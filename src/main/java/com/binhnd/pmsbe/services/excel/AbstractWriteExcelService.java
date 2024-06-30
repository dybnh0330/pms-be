package com.binhnd.pmsbe.services.excel;

import com.binhnd.pmsbe.common.constants.PMSExcelConstant;
import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.entity.HeaderColumnExcel;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.binhnd.pmsbe.common.utils.ExcelUtils.convertVoToMap;
import static org.apache.poi.ss.util.CellUtil.createCell;

public abstract class AbstractWriteExcelService implements WriteExcelAction {

    private static final int COLUMN_NO_DEFAULT = 1;
    private final String format = PMSExcelConstant.FORMAT_DAY_HOUR;
    private final String timezone = PMSExcelConstant.TIMEZONE_VIETNAM;
    private Map<String, HeaderColumnExcel> headerMap;
    private Map<String, DataValidationConstraint> validationConstraintCache = new HashMap<>();
    private CellStyle titleStyle, tableHeaderStyle, tableHeaderErrorStyle, tableBodyStyle, tableBodyErrorStyle, footerStyle;
    private SXSSFWorkbook wb;
    private SXSSFSheet sheet;
    private int rowNum = 0;
    private boolean writeValidation = false;

    public void setHeaderMap(Map<String, HeaderColumnExcel> headerMap) {
        this.headerMap = headerMap;
    }

    private void drawTitle() {
        Row row = sheet.createRow(rowNum);
        row.setHeight(getTitleHeight());
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, COLUMN_NO_DEFAULT + 1, headerMap.size()));
        createCell(row, COLUMN_NO_DEFAULT + 1, getTitle(), titleStyle);
        rowNum++;
    }

    private void drawHeader() {
        Row row = sheet.createRow(rowNum);
        row.setHeight(getHeaderHeight());
        int colIndex = COLUMN_NO_DEFAULT;

        if (headerMap != null) {
            for (String key : headerMap.keySet()) {
                boolean isError = headerMap.get(key).isError();
                createCell(row, colIndex++, headerMap.get(key).getColumnName(), isError ? tableHeaderErrorStyle : tableHeaderStyle);
            }
        }
        rowNum++;
    }

    private CellStyle createFooterStyle(SXSSFWorkbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);

        Font font = wb.createFont();
        font.setFontName(PMSExcelConstant.TIMES_NEW_ROMAN_FONT);
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        cellStyle.setFont(font);

        return cellStyle;
    }

    private CellStyle createTableBodyStyle(SXSSFWorkbook wb, boolean isError) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);

        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName(PMSExcelConstant.TIMES_NEW_ROMAN_FONT);

        if (isError) {
            font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        } else {
            font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        }

        cellStyle.setFont(font);

        return cellStyle;
    }

    private CellStyle createTableHeaderStyle(SXSSFWorkbook wb, boolean isError) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setWrapText(true);

        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setFontName(PMSExcelConstant.TIMES_NEW_ROMAN_FONT);

        if (isError) {
            font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        } else {
            font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        }

        cellStyle.setFont(font);

        return cellStyle;
    }

    private CellStyle createDocumentHeaderStyle(SXSSFWorkbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        Font font = wb.createFont();

        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);

        font.setFontName(PMSExcelConstant.TIMES_NEW_ROMAN_FONT);
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        cellStyle.setFont(font);

        return cellStyle;
    }

    private DataValidationConstraint createDataValidationWithHiddenSheet(Sheet sheet, String[] constraints, String name, String colValidation) {
        Sheet hiddenSheet = (wb.getSheet("hidden") == null) ? wb.createSheet("hidden") : wb.getSheet("hidden");
        int colIndex = CellReference.convertColStringToIndex(colValidation);
        for (int i = 0, length = constraints.length; i < length; i++) {
            String value = constraints[i];
            Row row = hiddenSheet.getRow(i) == null ? hiddenSheet.createRow(i) : hiddenSheet.getRow(i);
            Cell cell = row.createCell(colIndex);
            cell.setCellValue(value);
        }
        Name namedCell;
        if (wb.getName(name) == null) {
            namedCell = wb.createName();
            namedCell.setNameName(name);
        } else {
            namedCell = wb.getName(name);
        }
        String formula = "hidden!$XXX$1:$XXX$" + constraints.length;
        formula = formula.replace("XXX", colValidation);
        namedCell.setRefersToFormula(formula);
        //hide the hiddenSheet
        wb.setSheetHidden(wb.getSheetIndex(hiddenSheet), true);
        return sheet.getDataValidationHelper().createFormulaListConstraint(formula);
    }

    private DataValidation createValidation(Sheet sheet, String name, Set<String> constraintSet, String colValidation, int firstRow, int lastRow, int firstCol, int lastCol) {
        String[] constraints = constraintSet.toArray(new String[0]);
        DataValidationHelper helper = sheet.getDataValidationHelper();
        CellRangeAddressList rangeAddress = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);

        DataValidationConstraint dataValidationConstraint;
        if (ObjectUtils.isEmpty(validationConstraintCache.get(name))) {
            dataValidationConstraint = StringUtil.isEmpty(colValidation) ? helper.createExplicitListConstraint(constraints)
                    : createDataValidationWithHiddenSheet(sheet, constraints, name, colValidation);
            validationConstraintCache.put(name, dataValidationConstraint);
        } else {
            dataValidationConstraint = validationConstraintCache.get(name);
        }

        DataValidation dataValidation = helper.createValidation(dataValidationConstraint, rangeAddress);
        dataValidation.setShowErrorBox(true);
        dataValidation.setSuppressDropDownArrow(true);
        dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
        dataValidation.setShowPromptBox(true);

        return dataValidation;
    }

    @Override
    public void createWriter() {
        wb = new SXSSFWorkbook(1);
        sheet = wb.createSheet(getSheetName());
        rowNum = 0;

        this.titleStyle = createDocumentHeaderStyle(wb);

        this.tableHeaderStyle = createTableHeaderStyle(wb, false);

        this.tableHeaderErrorStyle = createTableHeaderStyle(wb, true);

        this.tableBodyStyle = createTableBodyStyle(wb, false);

        this.tableBodyErrorStyle = createTableBodyStyle(wb, true);

        this.footerStyle = createFooterStyle(wb);

        drawTitle();
        drawHeader();
    }

    @Override
    public <T> void appendData(List<T> datas) {
        if (ObjectUtils.isEmpty(datas)) {
            return;
        }

        for (T data : datas) {
            int colIndex = COLUMN_NO_DEFAULT;
            Row row = sheet.createRow(rowNum);
            String value;

            if (!ObjectUtils.isEmpty(headerMap)) {
                for (String key : headerMap.keySet()) {
                    boolean isError = headerMap.get(key).isError();

                    Map<String, String> map = convertVoToMap(data, format, timezone);

                    createCell(row, colIndex++, map.get(key) == null ? StringUtil.EMPTY : map.get(key), isError ? tableBodyErrorStyle : tableBodyStyle);

                    if (!writeValidation && !CollectionUtils.isEmpty(headerMap.get(key).getDataValidationSet())) {
                        DataValidation dataValidation = createValidation(
                                sheet,
                                key,
                                headerMap.get(key).getDataValidationSet(),
                                headerMap.get(key).getColValidation(),
                                row.getRowNum(),
                                row.getRowNum(),
                                colIndex - 1,
                                colIndex - 1);
                        sheet.addValidationData(dataValidation);
                        writeValidation = true;
                    }
                }
            }
            rowNum++;
        }
    }

    @Override
    public Workbook write() {
        drawFooter();
        this.validationConstraintCache.clear();

        return wb;
    }
}
