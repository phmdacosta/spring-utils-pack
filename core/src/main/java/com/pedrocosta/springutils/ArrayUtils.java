package com.pedrocosta.springutils;

import java.util.*;

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

    public static Object[] removeNulls(Object[] array) {
        Set<Object> result = new HashSet<>();
        for (Object o : array) {
            if (o != null) {
                result.add(o);
            }
        }
        return result.toArray(new Object[0]);
    }

    public static Object[] removeDuplicates(Object[] array) {
        return new HashSet<>(Arrays.asList(array)).toArray(new Object[0]);
    }
}
