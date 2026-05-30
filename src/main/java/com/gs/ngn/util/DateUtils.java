package com.gs.ngn.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private DateUtils() {}

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(FORMATTER);
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
}
