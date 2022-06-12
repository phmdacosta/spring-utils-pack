package com.pedrocosta.springutils;

public class ArrayUtils {
    public static boolean isEmpty(Object[] array) {
        if (array == null || array.length == 0) {
            return true;
        }
        boolean empty = true;
        for (Object o : array) {
            if (o != null) {
                empty = false;
                break;
            }
        }
        return empty;
    }
}
