package com.pedrocosta.springutils;

import com.pedrocosta.springutils.collection.CollectionUtils;
import com.pedrocosta.springutils.output.Log;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * A component to verify if two objects are equals even if they don't have "equals" method implemented.
 * The implementation of "equals" method is still the most recommended process, and it works within this component.
 */
public class ObjectUtils extends org.springframework.util.ObjectUtils {

    /**
     * Check if two objects are equals, comparing a specific field. It verifies only the parameter property.
     * @param o1    First object
     * @param o2    Second object
     * @param propertyName Name of the field to check
     * @return  True if the property of both objects has equal values, false otherwise.
     */
    public static boolean equals(final Object o1, final Object o2, final String propertyName) {
        if (deepEquals(o1, o2)) {
            return true;
        }

        if (o1 == null || o2 == null) {
            return false;
        }

        if (ClassUtils.isSimpleProperty(o1.getClass())) {
            return false;
        }

        if (o1.getClass().isArray()) {
            return ArrayUtils.equals((Object[]) o1, (Object[]) o2);
        }

        if (Collection.class.isAssignableFrom(o1.getClass())) {
            return CollectionUtils.equals((Collection<?>) o1, (Collection<?>) o2, propertyName);
        }

        Object fieldValueObj1 = getPropertyValue(o1, propertyName);
        Object fieldValueObj2 = getPropertyValue(o2, propertyName);

        return equals(fieldValueObj1, fieldValueObj2);
    }

    /**
     * Check if two objects are equals. It verifies all properties of these objects.
     * If the first parameter object has 'equals' method implemented, it returns the result of this method.
     * @param o1    First object
     * @param o2    Second object
     * @return  True if all properties of both objects have equal values, false otherwise.
     */
    public static boolean equals(final Object o1, final Object o2) {
        if (o1 == null && o2 == null) {
            return true;
        }

        if (o1 == null || o2 == null) {
            return false;
        }

        if (deepEquals(o1, o2)) {
            return true;
        }

        // If one object is simple property and not deeply equal, they are not equal.
        if (ClassUtils.isSimpleProperty(o1.getClass())) {
            return false;
        }

        if (o1.getClass().isArray()) {
            return ArrayUtils.equals((Object[]) o1, (Object[]) o2);
        }

        if (Collection.class.isAssignableFrom(o1.getClass())) {
            return CollectionUtils.equals((Collection<?>) o1, (Collection<?>) o2);
        }

        // For complex type
        if (hasEqualsOverride(o1.getClass())) {
            return o1.equals(o2);
        }

        boolean equals = false;
        Map<String, String> o1Properties = describeProperties(o1);
        for (Map.Entry<String, String> o1Entry : o1Properties.entrySet()) {
            equals = fieldEqualsInternal(o1, o2, o1Entry.getKey());
            if (!equals) {
                break;
            }
        }

        return equals;
    }

    /**
     * Check if two objects are exact equals. It looks if both object is the same instance or have exact the same value.
     * @param o1    First object
     * @param o2    Second object
     * @return  True if both objects are the same instance, false otherwise.
     */
    public static boolean deepEquals(final Object o1, final Object o2) {
        if (o1 == null && o2 == null) {
            return true;
        }

        if (o1 == null || o2 == null) {
            return false;
        }

        if (!o1.getClass().equals(o2.getClass())) {
            return false;
        }

        return Objects.deepEquals(o1, o2) || o1.equals(o2);
    }

    /**
     * Generates the hash code of an object.
     * @param object    Object
     * @return  Hash code
     */
    public static int hashCode(final Object object) {
        Map<String, String> properties = describeProperties(object);
        Object[] fieldsValues = new Object[properties.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            fieldsValues[i++] = getPropertyValue(object, entry.getKey());
        }

        return Objects.hash(fieldsValues);
    }

    /**
     * Compare two objects.
     * @param o1    First object
     * @param o2    Second object
     * @return  Zero if the both objects are equal, -1 if the first object is less than second, and positive 1 if it is greater.
     *          Null value is considered greater than anything.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static int compareTo(final Object o1, final Object o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }

        if (o1 == null) {
            return 1;
        }

        if (o2 == null) {
            return -1;
        }

        Comparable comp1 = (Comparable) o1;
        Comparable comp2 = (Comparable) o2;

        return comp1.compareTo(comp2);
    }

    /**
     * Compare the same property of two objects.
     * @param o1    First object
     * @param o2    Second object
     * @param propertyName  Name of the property to check
     * @return  Zero if the property value in both objects are equal, -1 if the property value of the first object is less than second, and positive 1 if it is greater.
     *          Null value is considered greater than anything.
     */
    public static int compareTo(final Object o1, final Object o2, final String propertyName) {
        if (o1 == null && o2 == null) {
            return 0;
        }

        if (o1 == null) {
            return 1;
        }

        if (o2 == null) {
            return -1;
        }

        Object fieldValueObj1 = getPropertyValue(o1, propertyName);
        if (fieldValueObj1 == null) {
            return 1;
        }
        Object fieldValueObj2 = getPropertyValue(o2, propertyName);
        if (fieldValueObj2 == null) {
            return -1;
        }

        return compareTo(fieldValueObj1, fieldValueObj2);
    }

    private static boolean fieldEqualsInternal(final Object o1, final Object o2, final String fieldName) {
        Object fieldValueObj1 = getPropertyValue(o1, fieldName);
        Object fieldValueObj2 = getPropertyValue(o2, fieldName);
        return equals(fieldValueObj1, fieldValueObj2);
    }

    private static boolean hasEqualsOverride(final Class<?> clazz) {
        return ClassUtils.hasDeclaredMethod(clazz, "equals");
    }

    private static Field getField(final Class<?> clazz, final String fieldName) throws NoSuchFieldException {
        Field field = null;
        Class<?> superClazz = clazz;
        while (field == null && superClazz != null) {
            try {
                field = superClazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                superClazz = superClazz.getSuperclass();
            }
        }

        if (field == null) {
            throw new NoSuchFieldException(fieldName);
        }

        return field;
    }

    private static Object getPropertyValue(final Object obj, final String propertyName) {
        PropertyDescriptor propDescriptor = BeanUtils.getPropertyDescriptor(obj.getClass(), propertyName);
        if (propDescriptor != null) {
            Method readMethod = propDescriptor.getReadMethod();
            if (readMethod != null) {
                try {
                    return readMethod.invoke(obj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    Log.error(ObjectUtils.class,
                            String.format("Could not find read method for property %s", propertyName));
                }
            }
        }

        try {
            return getField(obj.getClass(), propertyName).get(obj);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Log.error(ObjectUtils.class,
                    String.format("Could not access property %s", propertyName));
        }

        return null;
    }

    private static Map<String, String> describeProperties(final Object object) {
        try {
            return org.apache.commons.beanutils.BeanUtils.describe(object);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Log.error(ObjectUtils.class, String.format("Could not access %s properties.", object.getClass().getSimpleName()));
        }
        return new HashMap<>();
    }
}
