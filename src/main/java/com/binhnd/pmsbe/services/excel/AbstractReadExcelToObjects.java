package com.binhnd.pmsbe.services.excel;

import com.binhnd.pmsbe.common.utils.ExcelUtils;
import com.binhnd.pmsbe.common.utils.StringUtil;
import com.binhnd.pmsbe.common.utils.excel_parser.ExcelReaderHandler;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.util.ObjectUtils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractReadExcelToObjects {

    private Map<String, String> headerMapWithColumn;

    public <T> List<T> readFromFile(InputStream inputStream, Class<T> clazz, Map<String, String> headerMap) throws Exception {
        headerMapWithColumn = null;
        ZipSecureFile.setMinInflateRatio(0);

        ExcelReaderHandler readerHandler = new ExcelReaderHandler();
        readerHandler.setSheet(getSheetName());

        List<T> objects = new ArrayList<>();

        final boolean[] checkHeader = new boolean[1];
        final int headerMapSize = headerMap.size();

        readerHandler.onGetRow((rowIndex, rowValue) -> {
            if (ObjectUtils.isEmpty(rowValue)) {
                return;
            }

            if (!checkHeader[0] && checkIfRowContainHeaderTable(rowValue, headerMap)) {
                checkHeader[0] = true;
                return;
            }

            T t = convertVoToObject(headerMapWithColumn, clazz, rowValue);

            if (!ObjectUtils.isEmpty(t)) {
                objects.add(t);
            }
        });

        readerHandler.readExcelFile(inputStream);

        return objects;
    }

    public abstract String getSheetName();

    private boolean checkIfRowContainHeaderTable(
            Map<String, String> cellValue,
            Map<String, String> headerToObjectFieldMaps) {
        Map<String, String> mapField = new HashMap<>();

        String[] keySet = headerToObjectFieldMaps.keySet().toArray(new String[]{});

        List<String> headerTitle = Arrays.stream(keySet).map(this::simplifyString)
                .collect(Collectors.toList());

        for (String key : cellValue.keySet()) {
            int index = headerTitle.indexOf(simplifyString(cellValue.get(key)));
            if (!StringUtil.isTrimEmpty(cellValue.get(key)) && index >= 0) {
                mapField.put(headerToObjectFieldMaps.get(keySet[index]), key);
            }
        }

        if (mapField.size() < headerToObjectFieldMaps.size()) return false;

        headerMapWithColumn = mapField;

        return true;
    }

    private String simplifyString(String x) {
        if (StringUtil.isTrimEmpty(x)) return x;
        return x.toLowerCase().trim().replace(" +", " ");
    }

    private <T> T convertVoToObject(
            Map<String, String> fieldWithColumnIds,
            Class<T> clazz,
            Map<String, String> cellValues)
            throws InstantiationException, IllegalAccessException {
        T obj = clazz.newInstance();
        Field[] fieldArr = obj.getClass().getDeclaredFields();

        if (ObjectUtils.isEmpty(fieldWithColumnIds)) {
            return null;
        }

        try {
            for (Field field : fieldArr) {
                String name = field.getName();
                field.setAccessible(true);
                if (!fieldWithColumnIds.containsKey(name)) {
                    continue;
                }

                String key = fieldWithColumnIds.get(name);

                if (!cellValues.containsKey(key)) {
                    continue;
                }

                Class<?> typeName = field.getType();
                if (ExcelUtils.isPrimitiveNumbers(typeName)) {
                    ExcelUtils.setPrimitiveNumbers(field, obj, cellValues.get(key));
                } else {
                    ExcelUtils.setObject(field, obj, cellValues.get(key));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

}
