package com.pedrocosta.springutils;

import org.springframework.lang.Nullable;

/**
 * Utility class to retrieve application properties.
 * <br>
 * It loads all properties from <b><code>application.properties</code></b> file.
 * <br>
 * It also can be extended to change properties file name by overriding
 * {@linkplain AppProperties#getPropertiesFileName()} method.
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class AppProperties extends PropertiesUtils {

    private static final String DEFAULT_PROP_FILE_NAME = "application.properties";
    private static final Object locker = new Object();

    private static AppProperties instance;

    public static AppProperties instance(String propFileName) {
        AppProperties instance = new AppProperties();
        instance.loadProperties(propFileName);
        return instance;
    }

    @Nullable
    public static String get(String key) {
        synchronized (locker) {
            if (instance == null) {
                instance = instance(getPropertiesFileName());
            }
            return instance.getProperty(key);
        }
    }

    protected static String getPropertiesFileName() {
        return DEFAULT_PROP_FILE_NAME;
    }
}
