package com.pedrocosta.springutils;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.lang.Nullable;

import java.io.IOException;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class AppProperties extends PropertiesUtils {

    private static final String DEFAULT_PROP_FILE_NAME = "application.properties";
    private static final Object locker = new Object();

    private static AppProperties instance;

    public static AppProperties instance(String propFileName) {
        AppProperties instance = new AppProperties();
        instance.initProperties(propFileName);
        return instance;
    }

    @Nullable
    public static String get(String key) {
        synchronized (locker) {
            if (instance == null) {
                instance = instance(DEFAULT_PROP_FILE_NAME);
            }
            return instance.getProperty(key);
        }
    }
}