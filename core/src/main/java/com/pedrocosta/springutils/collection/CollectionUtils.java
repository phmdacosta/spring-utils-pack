package com.pedrocosta.springutils.collection;

import com.pedrocosta.springutils.ObjectUtils;
import com.pedrocosta.springutils.collection.comparators.DefaultComparator;

import java.util.*;

public class CollectionUtils extends org.springframework.util.CollectionUtils {

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
     * Sort a list by many properties values.
     * @param list              List to sort
     * @param properties        Properties to looking for when sort
     * @param asc               For each property, check if they must be sorted ascending or not
     * @param nullValuesBefore  Check if null values come first (for all properties)
     */
    public static <T> void sort(List<T> list, final String[] properties, final boolean[] asc, final boolean nullValuesBefore) {
        if (list == null || list.isEmpty()) {
            return;
        }
        sort(list, new DefaultComparator<>(properties, asc, nullValuesBefore));
    }

    /**
     * Sort a list by a specific property value.
     * @param list
     * @param property
     * @param <T>
     */
    public static <T> void sort(List<T> list, final String property, final boolean asc, final boolean nullValuesBefore) {
        String[] properties = {property};
        boolean[] ascArr = {asc};
        sort(list, properties, ascArr, nullValuesBefore);
    }

    /**
     * Sort a list by a specific property value.
     * @param list
     * @param property
     * @param <T>
     */
    public static <T> void sort(List<T> list, final String property) {
        sort(list, property, true, false);
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

    public <T> T search(final Collection<T> collection, final String property, final Object value) {
        return search(collection, property, Operator.EQUALS_THEN, value);
    }

    public <T> T search(final Collection<T> collection, final String property, final Operator operator, final Object value) {
        if (value == null) {
            return null;
        }
        return collection.stream()
                .filter(obj -> filterSearch(obj, property, operator, value))
                .findAny().orElse(null);
    }

    public <T> boolean contains(final Collection<T> collection, final String property, final Object value) {
        T found = collection.stream()
                .filter(obj -> filterSearch(obj, property, Operator.EQUALS_THEN, value))
                .findAny().orElse(null);
        return found != null;
    }

    private boolean filterSearch(Object obj, String property, Operator operator, Object value) {
        Object objPropValue = ObjectUtils.getPropertyValue(obj, property);
        int resultCompare = ObjectUtils.compareTo(value, objPropValue);
        boolean result;

        switch (operator) {
            case LESS_THEN:
                result = resultCompare < 0;
                break;
            case LESS_OR_EQUALS_THEN:
                result = resultCompare <= 0;
                break;
            case GREATER_THEN:
                result = resultCompare > 0;
                break;
            case GREATER_OR_EQUALS_THEN:
                result = resultCompare >= 0;
                break;
            case NOT_EQUALS_THEN:
                result = resultCompare != 0;
                break;
            case EQUALS_THEN:
            default:
                result = resultCompare == 0;
        }

        return result;
    }

}
