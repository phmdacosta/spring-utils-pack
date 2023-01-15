package com.pedrocosta.springutils;

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
     * @param fieldName Name of the field to check
     * @return  True if the property of both objects has equal values, false otherwise.
     */
    public static boolean equals(Object o1, Object o2, String fieldName) {
        if (deepEquals(o1, o2)) {
            return true;
        }

        if (o1 == null || o2 == null) {
            return false;
        }

        if (ClassUtils.isSimpleProperty(o1.getClass())) {
            return false;
        }

        if (o1.getClass().isArray() && equals((Object[]) o1, (Object[]) o2)) {
            return true;
        }

        Object fieldValueObj1 = getPropertyValue(o1, fieldName);
        Object fieldValueObj2 = getPropertyValue(o2, fieldName);

        return equals(fieldValueObj1, fieldValueObj2);
    }

    /**
     * Check if two objects are equals. It verifies all properties of these objects.
     * If the first parameter object has 'equals' method implemented, it returns the result of this method.
     * @param o1    First object
     * @param o2    Second object
     * @return  True if all properties of both objects have equal values, false otherwise.
     */
    public static boolean equals(Object o1, Object o2) {
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

        if (o1.getClass().isArray() && equals((Object[]) o1, (Object[]) o2)) {
            return true;
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
     * Check if two collections are equals. It verifies all elements of them.
     * @param o1    First collection
     * @param o2    Second collection
     * @return  True if all elements of both collections are equal, false otherwise.
     */
    public static boolean equals(Collection<?> o1, Collection<?> o2) {
        return equals(o1.toArray(new Object[0]), o2.toArray(new Object[0]));
    }

    /**
     * Check if two collections are equals, comparing a specific field. It verifies all elements of them.
     * @param o1    First collection
     * @param o2    Second collection
     * @param fieldName Name of the field to check
     * @return  True if all elements of both collections have the specific property with equal value, false otherwise.
     */
    public static boolean equals(Collection<?> o1, Collection<?> o2, String fieldName) {
        return equals(o1.toArray(new Object[0]), o2.toArray(new Object[0]), fieldName);
    }

    /**
     * Check if two array are equals. It verifies all elements of them.
     * @param o1    First array
     * @param o2    Second array
     * @return  True if all elements of both arrays are equal, false otherwise.
     */
    public static boolean equals(Object[] o1, Object[] o2) {
        if (!isValidArray(o1) || !isValidArray(o2)) {
            return false;
        }

        if (o1.length != o2.length) {
            return false;
        }

        boolean equals = true;
        for (Object elem1 : o1) {
            for (Object elem2 : o2) {
                if(!equals(elem1, elem2)) {
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
     * @param fieldName Name of the field to check
     * @return  True if all elements of both arrays have the specific property with equal value, false otherwise.
     */
    public static boolean equals(Object[] o1, Object[] o2, String fieldName) {
        if (!isValidArray(o1) || !isValidArray(o2)) {
            return false;
        }

        if (o1.length != o2.length) {
            return false;
        }

        boolean equals = true;
        for (Object elem1 : o1) {
            for (Object elem2 : o2) {
                if(!equals(elem1, elem2, fieldName)) {
                    equals = false;
                    break;
                }
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
    public static boolean deepEquals(Object o1, Object o2) {
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
    public static int hashCode(Object object) {
        Map<String, String> properties = describeProperties(object);
        Object[] fieldsValues = new Object[properties.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            fieldsValues[i++] = getPropertyValue(object, entry.getKey());
        }

        return Objects.hash(fieldsValues);
    }

    private static boolean fieldEqualsInternal(Object o1, Object o2, String fieldName) {
        Object fieldValueObj1 = getPropertyValue(o1, fieldName);
        Object fieldValueObj2 = getPropertyValue(o2, fieldName);
        return equals(fieldValueObj1, fieldValueObj2);
    }

    private static boolean isValidArray(Object[] objArray) {
        if (objArray == null) {
            return false;
        }
        if (!objArray.getClass().isArray()) {
            return false;
        }
        return true;
    }

    private static boolean hasEqualsOverride(Class<?> clazz) {
        return ClassUtils.hasDeclaredMethod(clazz, "equals");
    }

    private static Field getField(final Class<?> clazz, String fieldName) throws NoSuchFieldException {
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

    private static Object getPropertyValue(Object obj, String propertyName) {
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

    private static Map<String, String> describeProperties(Object object) {
        try {
            return org.apache.commons.beanutils.BeanUtils.describe(object);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Log.error(ObjectUtils.class, String.format("Could not access %s properties.", object.getClass().getSimpleName()));
        }
        return new HashMap<>();
    }
}
