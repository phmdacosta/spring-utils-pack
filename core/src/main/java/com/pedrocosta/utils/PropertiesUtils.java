package com.pedrocosta.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class PropertiesUtils {
    private Properties properties;

    public String getProperty(String key) {
        return this.getProperties().getProperty(key);
    }

    protected Properties getProperties() {
        return properties;
    }

    protected void initProperties(String propFileName) {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        this.properties = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(propFileName)) {
            properties.load(resourceStream);
        } catch (IOException | NullPointerException ignored) {
        }
    }
}
