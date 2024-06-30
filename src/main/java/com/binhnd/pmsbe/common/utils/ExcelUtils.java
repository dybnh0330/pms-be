package com.binhnd.pmsbe.common.utils;

import com.binhnd.pmsbe.common.enums.EnumAlignExcel;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ExcelUtils {

    public static final String EXCEL_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String TEXT_TYPE = "text/plain";
    private static final Set<Class<?>> primitiveNumbers = Stream
            .of(int.class, long.class, float.class,
                    double.class, byte.class, short.class)
            .collect(Collectors.toSet());

    private ExcelUtils() {

    }

    public static void setAlign(EnumAlignExcel align, CellStyle cellStyle) {
        switch (align) {
            case LEFT: {
                cellStyle.setAlignment(HorizontalAlignment.LEFT);
                break;
            }
            case RIGHT: {
                cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                break;
            }
            case CENTER: {
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                break;
            }
            default: {
                cellStyle.setAlignment(HorizontalAlignment.LEFT);
            }
        }
    }

    public static ByteArrayInputStream exportExcelByteArray(Workbook workbook) {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("Failed writing", e);
        }
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;

        // read bytes from the input stream and store them in the buffer
        while ((len = in.read(buffer)) != -1) {
            // write bytes from the buffer into the output stream
            os.write(buffer, 0, len);
        }

        return os.toByteArray();
    }

    public static ResponseEntity<InputStreamResource> sendExcelToClient(Workbook workbook, HttpServletResponse response, String fileName) {
        ByteArrayInputStream in = exportExcelByteArray(workbook);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        DateFormat dateFormatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String currentDateTime = dateFormatter.format(new Date());
        fileName = fileName + "_" + currentDateTime + ".xlsx";
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(fileName, StandardCharsets.UTF_8).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    /******************
     * /* private zone
     *****************/
    public static Map<String, String> convertVoToMap(Object obj, String format, String timezone) {
        Map<String, String> map = new HashMap<>();
        Field[] fieldArr = obj.getClass().getDeclaredFields();
        try {
            for (Field field : fieldArr) {
                String name = field.getName();
                String value = "";
                field.setAccessible(true);
                if (field.get(obj) instanceof Timestamp) {
                    value = (field.get(obj) != null) ? DateUtil.converterToString((Timestamp) field.get(obj), format)
                            : StringUtil.EMPTY;
                } else if (field.get(obj) instanceof Instant) {
                    value = (field.get(obj) != null) ? DateUtil.converterToString((Instant) field.get(obj), format, timezone)
                            : StringUtil.EMPTY;
                } else {
                    value = (field.get(obj) != null) ? field.get(obj).toString() : StringUtil.EMPTY;
                }

                if (value != null) {
                    map.put(name, value);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    public static <T> T convertVoToObject(Map<String, Integer> map, Class<T> clazz, Row row) throws InstantiationException, IllegalAccessException {
        T obj = clazz.newInstance();
        Field[] fieldArr = obj.getClass().getDeclaredFields();
        try {
            for (Field field : fieldArr) {
                String name = field.getName();
                field.setAccessible(true);
                if (map.get(name) == null) {
                    continue;
                }
                int rowId = map.get(name);
                Class<?> typeName = field.getType();
                if (isPrimitiveNumbers(typeName)) {
                    setPrimitiveNumbers(field, obj, row, rowId);
                } else {
                    setObject(field, obj, row, rowId);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    public static <T> T convertVoToObject(Map<String, Integer> map, Class<T> clazz, List<String> objectInLine)
            throws InstantiationException, IllegalAccessException {
        T obj = clazz.newInstance();
        Field[] fieldArr = obj.getClass().getDeclaredFields();
        try {
            for (Field field : fieldArr) {
                String name = field.getName();
                field.setAccessible(true);
                if (map.get(name) == null) {
                    continue;
                }
                int index = map.get(name);
                Class<?> typeName = field.getType();
                if (ExcelUtils.isPrimitiveNumbers(typeName)) {
                    setPrimitiveNumbers(field, obj, objectInLine.get(index));
                } else {
                    setObject(field, obj, objectInLine.get(index));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    private static void setPrimitiveNumbers(Field field, Object obj, Row row, int rowId) throws IllegalAccessException {
        String value = getCellValue(row.getCell(rowId));
        if (field.get(obj) instanceof Boolean) {
            field.setBoolean(obj, row.getCell(rowId).getBooleanCellValue());
        } else if (field.get(obj) instanceof Integer) {
            field.setInt(obj, Integer.parseInt(value));
        } else if (field.get(obj) instanceof Long) {
            field.setLong(obj, Long.parseLong(value));
        } else if (field.get(obj) instanceof Double) {
            field.setDouble(obj, Double.parseDouble(value));
        } else if (field.get(obj) instanceof Float) {
            field.setFloat(obj, Float.parseFloat(value));
        }
    }

    public static void setPrimitiveNumbers(Field field, Object obj, String value) throws IllegalAccessException {
        if (field.get(obj) instanceof Boolean) {
            field.setBoolean(obj, Boolean.valueOf(value));
        } else if (field.get(obj) instanceof Integer) {
            field.setInt(obj, Integer.parseInt(value));
        } else if (field.get(obj) instanceof Long) {
            field.setLong(obj, Long.parseLong(value));
        } else if (field.get(obj) instanceof Double) {
            field.setDouble(obj, Double.parseDouble(value));
        } else if (field.get(obj) instanceof Float) {
            field.setFloat(obj, Float.parseFloat(value));
        }
    }

    private static void setObject(Field field, Object obj, Row row, int rowId) throws IllegalAccessException {
        try {
            Class<?> typeName = field.getType();
            String value = getCellValue(row.getCell(rowId));
            if (CellType.NUMERIC == row.getCell(rowId).getCellType()) {
                double valueNumber = row.getCell(rowId).getNumericCellValue();
                if (Integer.class.equals(typeName)) {
                    field.set(obj, (int) valueNumber);
                } else if (Long.class.equals(typeName)) {
                    field.set(obj, (long) valueNumber);
                } else if (Double.class.equals(typeName)) {
                    field.set(obj, valueNumber);
                } else if (Float.class.equals(typeName)) {
                    field.set(obj, (float) valueNumber);
                } else if (String.class.equals(typeName)) {
                    int number = (int) Double.parseDouble(value);
                    field.set(obj, String.valueOf(number));
                }
            } else if (CellType.BOOLEAN == row.getCell(rowId).getCellType()) {
                field.setBoolean(obj, row.getCell(rowId).getBooleanCellValue());
            } else {
                if (Integer.class.equals(typeName)) {
                    field.set(obj, Integer.parseInt(value));
                } else if (Long.class.equals(typeName)) {
                    field.set(obj, Long.parseLong(value));
                } else if (Double.class.equals(typeName)) {
                    field.set(obj, Double.parseDouble(value));
                } else if (Float.class.equals(typeName)) {
                    field.set(obj, Float.parseFloat(value));
                } else {
                    field.set(obj, value);
                }
            }
        } catch (NumberFormatException ex) {
            field.set(obj, 0);
        }

    }

    public static void setObject(Field field, Object obj, String value) throws IllegalAccessException {
        try {
            Class<?> typeName = field.getType();
            if (Integer.class.equals(typeName)) {
                field.set(obj, Integer.parseInt(value));
            } else if (Long.class.equals(typeName)) {
                field.set(obj, Long.parseLong(value));
            } else if (Double.class.equals(typeName)) {
                field.set(obj, Double.parseDouble(value));
            } else if (Float.class.equals(typeName)) {
                field.set(obj, Float.parseFloat(value));
            } else {
                field.set(obj, value);
            }
        } catch (NumberFormatException ex) {
            field.set(obj, 0);
        }

    }

    public static boolean isPrimitiveNumbers(Class<?> cls) {
        if (cls.isPrimitive()) {
            return primitiveNumbers.contains(cls);
        }
        return false;
    }

    private static Object cloneObject(Object obj) {
        try {
            Object clone = obj.getClass().newInstance();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                field.set(clone, field.get(obj));
            }
            return clone;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean checkIfRowIsEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell.toString())) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkIfRowContainHeader(Row row) {
        if (checkIfRowIsEmpty(row)) {
            return false;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum <= row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK
                    && StringUtils.isNotBlank(getCellValue(cell))
                    && cell.getCellStyle().getFillForegroundColor() == IndexedColors.AUTOMATIC.getIndex()
                    && cell.getCellStyle().getBorderTop() != BorderStyle.THIN) {
                return true;
            }
        }
        return false;
    }

    private static String getCellValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else return cell.getStringCellValue().trim();
    }

    public static boolean checkIfRowContainHeaderTable(Row row, List<String> headers) {
        if (checkIfRowIsEmpty(row)) {
            return false;
        }

        for (int cellNum = row.getFirstCellNum(); cellNum <= row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (!ObjectUtils.isEmpty(cell)) {
                String cellValue = getCellValue(cell);
                if (!headers.contains(StringUtil.removeWhitespace(cellValue).trim())) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean hasExcelFormat(MultipartFile file) {
        return EXCEL_TYPE.equals(file.getContentType());
    }
}
