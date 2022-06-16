package com.pedrocosta.springutils.jsonmanager.adapter;

import com.pedrocosta.springutils.PropertiesUtils;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdapterProperties extends PropertiesUtils {
    private static final String DEFAULT_PROP_FILE_NAME = "adapters.properties";
    private static final Object locker = new Object();

    private static AdapterProperties instance;

    public static AdapterProperties instance(String propFileName) {
        synchronized (locker) {
            if (instance == null) {
                instance = new AdapterProperties();
                instance.loadProperties(propFileName);
            }
            return instance;
        }
    }

    @Nullable
    public static String get(String key) {
        return instance(DEFAULT_PROP_FILE_NAME).getProperty(key);
    }

    public static List<String> getAll() {
        List<String> properties = new ArrayList<>();
        instance(DEFAULT_PROP_FILE_NAME).getProperties().forEach(
                (k, v) -> properties.add(k.toString()));
        return properties;
    }
}
