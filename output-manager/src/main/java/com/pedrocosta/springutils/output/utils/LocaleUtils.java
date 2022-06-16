package com.pedrocosta.springutils.output.utils;

import org.springframework.lang.Nullable;

import java.util.Locale;

public class LocaleUtils {
    @Nullable
    public static Locale fromString(String localeStr) {
        if (localeStr != null && !localeStr.isEmpty()) {
            String[] locInfo = localeStr.split("_");
            if (locInfo.length > 1) {
                if (locInfo.length > 2) {
                    return new Locale(locInfo[0], locInfo[1], locInfo[2]);
                }
                return new Locale(locInfo[0], locInfo[1]);
            }
            return new Locale(localeStr);
        }
        return null;
    }
}
