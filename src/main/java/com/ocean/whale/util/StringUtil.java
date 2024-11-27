package com.ocean.whale.util;

public class StringUtil {
    public static boolean isNotNullOrBlank(String str) {
        return str != null && !str.isBlank();
    }

    public static boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }
}
