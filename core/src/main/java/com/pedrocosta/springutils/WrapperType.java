package com.pedrocosta.springutils;

import java.util.Arrays;

public enum WrapperType {
    BOOLEAN(Boolean.class, false),
    CHAR(Character.class, '\0'),
    BYTE(Byte.class, Byte.valueOf("0")),
    SHORT(Short.class, 0),
    INTEGER(Integer.class, 0),
    LONG(Long.class, 0L),
    FLOAT(Float.class, 0.0),
    DOUBLE(Double.class, 0.0D),
    STRING(String.class, null);

    final Class<?> clazz;
    final Object initValue;

    WrapperType(Class<?> clazz, Object initValue) {
        this.clazz = clazz;
        this.initValue = initValue;
    }

    public static boolean is(Class<?> clazz) {
        return Arrays.stream(WrapperType.values()).
                anyMatch(type -> type.clazz.equals(clazz));
    }

    public static WrapperType get(Class<?> clazz) {
        return Arrays.stream(WrapperType.values())
                .filter(type -> type.clazz.equals(clazz))
                .findAny().orElse(null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T init(Class<T> clazz) {
        WrapperType wrapperType = get(clazz);
        return (T) wrapperType.initValue;
    }
}
