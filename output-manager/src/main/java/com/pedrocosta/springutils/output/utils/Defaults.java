package com.pedrocosta.springutils.output.utils;

import com.pedrocosta.springutils.AppProperties;

import java.util.Locale;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class Defaults {
    public static final Locale LOCALE = getDefaultLocale();

    private static Locale getDefaultLocale() {
        Locale locale = LocaleUtils.fromString(AppProperties.get("spring.messages.locale"));
        return locale != null ? locale : Locale.US;
    }
}
