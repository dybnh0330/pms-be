package com.binhnd.pmsbe.common.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtil {

    private DateUtil() {

    }

    public static String converterToString(Instant instant, String format, String timezone) {
        String dateTimeFormat = "";

        if ("YYYY-MM-DD".equals(format)) {
            dateTimeFormat = "yyyy-MM-dd ";
        } else {
            dateTimeFormat = "dd-MM-yyyy ";
        }

        if ("HH:mm:ss".equals(format)) {
            dateTimeFormat += "HH:mm:ss";
        } else {
            dateTimeFormat += "a hh:mm:ss";
        }

        return instant.atZone(ZoneId.of(timezone))
                .format(DateTimeFormatter.ofPattern(dateTimeFormat).withLocale(Locale.ENGLISH));
    }

    public static String converterToString(Timestamp instant, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(instant);
    }
}
