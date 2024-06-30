package com.binhnd.pmsbe.common.utils.excel_parser;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ExcelReaderHandler extends DefaultHandler {

    protected Map<String, String> header = new HashMap<>();

    protected Map<String, String> rowValues = new HashMap<>();

    protected SharedStringsTable sharedStringsTable;

    private OnGetRowListener listener;

    protected long rowNumber = 0;

    private String cellId;

    private String contents;

    private Boolean isCellValues;

    private Boolean fromStt;

    private String sheet;

    private final Logger L = LoggerFactory.getLogger(ExcelReaderHandler.class);

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    protected static String getColumnId(String attribute) throws SAXException {
        for (int i = 0; i < attribute.length(); i++) {
            if (!Character.isAlphabetic(attribute.charAt(i))) {
                return attribute.substring(0, i);
            }
        }
        throw new SAXException("Invalid format " + attribute);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        contents = "";

        switch (qName) {
            case "row": {
                String rowNumStr = attributes.getValue("r");
            }
            break;

            case "c": {
                cellId = getColumnId(attributes.getValue("r"));

                String cellType = attributes.getValue("t");
                if (cellType != null) {
                    if (cellType.equals("s")) {
                        fromStt = true;
                    } else if (cellType.equals("inlineStr")) {
                        fromStt = false;
                    }
                }
            }
            break;
            case "v":
                isCellValues = true;
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "c":
                if (isCellValues && fromStt) {
                    int index = Integer.parseInt(contents);
                    contents = new XSSFRichTextString(sharedStringsTable.getItemAt(index).getString()).toString();
                    rowValues.put(cellId, contents);
                    cellId = null;
                    isCellValues = false;
                    fromStt = false;
                } else {
                    rowValues.put(cellId, contents);
                    isCellValues = false;
                }
                break;
            case "row": {
                header.clear();
                if (rowNumber == 1) {
                    header.putAll(rowValues);
                }
                try {
                    processRow();
                } catch (ExecutionException | InterruptedException | IllegalAccessException |
                         InstantiationException e) {
                    L.error("Read Excel Exception ", e);

                    if (e instanceof InterruptedException) {
                        Thread.currentThread().interrupt();
                    }
                }
                rowValues.clear();
            }
            break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isCellValues || !fromStt) {
            contents += new String(ch, start, length);
        }
    }

    protected boolean processSheet(String sheetName) {
        if (!ObjectUtils.isEmpty(sheet)) {
            return sheetName.equals(sheet);
        }
        return true;
    }

    private void processRow() throws InstantiationException, IllegalAccessException, ExecutionException, InterruptedException {
        if (listener != null) {
            listener.onGetRow(rowNumber, rowValues);
        }
    }

    public void readExcelFile(InputStream file) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        try (OPCPackage opcPackage = OPCPackage.open(file)) {
            XSSFReader xssfReader = new XSSFReader(opcPackage);
            sharedStringsTable = (SharedStringsTable) xssfReader.getSharedStringsTable();

            Iterator<InputStream> sheets = xssfReader.getSheetsData();

            XSSFReader.SheetIterator sheetIterator;

            if (sheets instanceof XSSFReader.SheetIterator) {
                sheetIterator = (XSSFReader.SheetIterator) sheets;

                while (sheetIterator.hasNext()) {
                    try (InputStream sheet = sheetIterator.next()) {
                        String sheetName = sheetIterator.getSheetName();
                        if (!processSheet(sheetName)) {
                            continue;
                        }
                        saxParser.parse(sheet, this);
                    }
                }
            }
        }
    }

    public void onGetRow(OnGetRowListener onGetRowListener) {
        this.listener = onGetRowListener;
    }
}
