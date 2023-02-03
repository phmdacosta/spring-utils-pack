package com.pedrocosta.springutils;

import java.util.*;

public class ArrayUtils {
    /**
     * Verify if array is empty.
     * @param array
     * @return  True if it is empty, false otherwise.
     */
    public static boolean isEmpty(final Object[] array) {
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

    /**
     * Remove <code>null</code> elements from array.
     * @param array
     * @return  New array without any null element. The array's length is also adapted.
     */
    public static Object[] removeNulls(final Object[] array) {
        Set<Object> result = new HashSet<>();
        for (Object o : array) {
            if (o != null) {
                result.add(o);
            }
        }
        return result.toArray(new Object[0]);
    }

    /**
     * Remove duplicated elements from array.
     * @param array
     * @return  New array without duplicated elements. The array's length is also adapted.
     */
    public static Object[] removeDuplicates(final Object[] array) {
        return new HashSet<>(Arrays.asList(array)).toArray(new Object[0]);
    }

    /**
     * Check if two array are equals. It verifies all elements of them.
     * @param o1    First array
     * @param o2    Second array
     * @return  True if all elements of both arrays are equal, false otherwise.
     */
    public static boolean equals(final Object[] o1, final Object[] o2) {
        if (o1 == null && o2 == null) {
            return true;
        }

        if (o1 == null || o2 == null) {
            return false;
        }

        if (o1.length != o2.length) {
            return false;
        }

        boolean equals = true;
        for (Object elem1 : o1) {
            for (Object elem2 : o2) {
                if(!ObjectUtils.equals(elem1, elem2)) {
                    equals = false;
                    break;
                }
            }
        }

        return equals;
    }

    /**
     * Check if two arrays are equals, comparing a specific field. It verifies all elements of them.
     * @param o1    First array
     * @param o2    Second array
     * @param propertyName Name of the field to check
     * @return  True if all elements of both arrays have the specific property with equal value, false otherwise.
     */
    public static boolean equals(final Object[] o1, final Object[] o2, final String propertyName) {
        if (o1 == null && o2 == null) {
            return true;
        }

        if (o1 == null || o2 == null) {
            return false;
        }

        if (o1.length != o2.length) {
            return false;
        }

        boolean equals = true;
        for (Object elem1 : o1) {
            for (Object elem2 : o2) {
                if(!ObjectUtils.equals(elem1, elem2, propertyName)) {
                    equals = false;
                    break;
                }
            }
        }

        return equals;
    }
}
