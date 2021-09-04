package com.yam.app.common;

public final class StringUtils {

    private StringUtils() {
    }

    /**
     * 입력값이 null 또는 공백이라면 true 반환.
     */
    public static boolean isBlank(String value) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        return value.trim().isEmpty();
    }
}
