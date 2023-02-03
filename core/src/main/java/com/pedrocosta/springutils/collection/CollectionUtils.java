package com.pedrocosta.springutils.collection;

import com.pedrocosta.springutils.ObjectUtils;

import java.util.*;

public class CollectionUtils {

    /**
     * Sort a list using a comparator.
     * @param list
     * @param comparator
     * @param <T>
     */
    public static <T> void sort(List<T> list, final Comparator<T> comparator) {
        if (list != null) {
            list.sort(comparator);
        }
    }

    /**
     * Sort a list by a specific property value.
     * @param list
     * @param property
     * @param <T>
     */
    public static <T> void sort(List<T> list, final String property) {
        if (list == null || list.isEmpty()) {
            return;
        }
        Comparator<T> comparator = (o1, o2) -> ObjectUtils.compareTo(o1, o2, property);
        sort(list, comparator);
    }

    /**
     * Check if two collections are equal.
     * @param o1
     * @param o2
     * @return  True if all elements in both collections are equal, false otherwise.
     */
    public static boolean equals(final Collection<?> o1, final Collection<?> o2) {
        if (o1 == null && o2 == null) {
            return true;
        }

        if (o1 == null || o2 == null) {
            return false;
        }

        if (o1.size() != o2.size()) {
            return false;
        }

        Iterator<?> iterator1 = o1.iterator();
        Iterator<?> iterator2 = o2.iterator();
        while (iterator1.hasNext()) {
            boolean isEqual = ObjectUtils.equals(iterator1.next(), iterator2.next());
            if (!isEqual) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if two collections are equal.
     * @param o1
     * @param o2
     * @return  True if all elements in both collections are equal, false otherwise.
     */
    public static boolean equals(final Collection<?> o1, final Collection<?> o2, final String property) {
        if (o1 == null && o2 == null) {
            return true;
        }

        if (o1 == null || o2 == null) {
            return false;
        }

        if (o1.size() != o2.size()) {
            return false;
        }

        Iterator<?> iterator1 = o1.iterator();
        Iterator<?> iterator2 = o2.iterator();
        while (iterator1.hasNext()) {
            boolean isEqual = ObjectUtils.equals(iterator1.next(), iterator2.next(), property);
            if (!isEqual) {
                return false;
            }
        }

        return true;
    }

}
