package com.pedrocosta.utils;

import java.lang.reflect.InvocationTargetException;

public class NumberUtils {
    public static boolean isDouble(double num) {
        return !((num % 1) == 0);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Number> T valueOf(String s, Class<T> classOfT) {
        try {
            return (T) classOfT.getMethod("valueOf", String.class).invoke(null, s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isNumberClass(Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz);
    }
}
