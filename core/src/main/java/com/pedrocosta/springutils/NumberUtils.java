package com.pedrocosta.springutils;

/**
 * Util class to handle numbers.
 *
 * @author Pedro H. M. da Costa
 * @version 1.0
 */
public class NumberUtils {
    public static boolean isDouble(final double num) {
        return !((num % 1) == 0);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Number> T valueOf(final String s, final Class<T> classOfT) {
        try {
            return (T) classOfT.getMethod("valueOf", String.class).invoke(null, s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isNumberClass(final Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz);
    }
}
