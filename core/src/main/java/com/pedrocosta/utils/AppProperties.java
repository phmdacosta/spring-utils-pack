package com.pedrocosta.utils;

import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class AppProperties {

    private static final String DEFAULT_PROP_FILE_NAME = "application.properties";
    private Properties properties;
    private String propFileName;

    private static AppProperties instance;

    public static AppProperties instance(String propFileName) {
        return new AppProperties()
                .setPropFileName(propFileName)
                .initProperties();
    }

    @Nullable
    public static synchronized String get(String key) {
        if (instance == null) {
            instance = instance(null);
        }
        return instance.getProperty(key);
    }

    public String getProperty(String key) {
        return this.getProperties().getProperty(key);
    }

    private Properties getProperties() {
        return properties;
    }

    private AppProperties setPropFileName(String fileName) {
        if ((fileName == null || fileName.isEmpty())) {
            this.propFileName = DEFAULT_PROP_FILE_NAME;
        } else if (!fileName.equals(propFileName)){
            this.propFileName = fileName;
        }
        return this;
    }

    private AppProperties initProperties() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        this.properties = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(propFileName)) {
            properties.load(resourceStream);
        } catch (IOException | NullPointerException ignored) {
        }
        return this;
    }
}
