package com.binhnd.pmsbe.common.utils;

import com.binhnd.pmsbe.common.enums.EnumGender;
import com.binhnd.pmsbe.common.enums.EnumPMSException;
import com.binhnd.pmsbe.common.exception.PMSException;
import com.binhnd.pmsbe.dto.request.SearchSortPageableDTO;

import java.util.regex.Pattern;

import static com.binhnd.pmsbe.common.constants.PMSConstants.FEMALE;
import static com.binhnd.pmsbe.common.constants.PMSConstants.MALE;

public final class StringUtil {
    public static final String EMPTY = "";

    static final Pattern INVALID_EXCEL_DATA = Pattern.compile("(^(\\s|\\t|\\r)*(=|\\+|-|@|^\\t|^\\n|^\\r)(\\s|\\t|\\r)*(\\w|\\W|\\d|\\D)*)|((\\w|\\W|\\d|\\D)*(['\",;])(\\s|\\t|\\r)*(=|\\+|-|@|^\\t|^\\n|^\\r)(\\w|\\W|\\d|\\D)*)"); //NOSONAR

    private StringUtil() {
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isTrimEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static boolean isEmpty(Object o) {
        return o == null || StringUtil.isEmpty(o.toString());
    }

    public static String uppercaseAndRemoveWhitespace(String str) {
        if (null == str) return str;
        return str.replaceAll("\\s\\s+", "").trim().toUpperCase();
    }

    public static String removeWhitespace(String str) {
        if (null == str) return str;
        return str.replaceAll("\\s\\s+", "").trim();
    }


    public static boolean isNotNullOrEmpty(String inputString) {
        return inputString != null && !inputString.isBlank() && !inputString.isEmpty() && !inputString.equals("undefined") && !inputString.equals("null") && !inputString.equals(" ");
    }

    public static void adjustSearchSort(SearchSortPageableDTO dto) {
        if (StringUtil.isTrimEmpty(dto.getSearchText())) {
            dto.setSearchText(StringUtil.EMPTY);
        }
        if (StringUtil.isTrimEmpty(dto.getSortColumn())) {
            dto.setSortColumn(StringUtil.EMPTY);
        }
        if (StringUtil.isTrimEmpty(dto.getSortDirection())) {
            dto.setSortDirection(StringUtil.EMPTY);
        }
    }

    public static String convertGenderToString(Long gender) {
        if (gender.equals(0L)) {
            return MALE;
        }
        if (gender.equals(1L)) {
            return FEMALE;
        }
        throw new PMSException(EnumPMSException.CONVERT_GENDER_ENUM_ERROR);
    }



    public static boolean isInvalidData(String input) {
        return INVALID_EXCEL_DATA.matcher(input).matches();
    }
}
