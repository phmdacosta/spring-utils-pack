package com.pedrocosta.springutils.collection.comparators;

import com.pedrocosta.springutils.ObjectUtils;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.sql.Timestamp;
import java.util.Comparator;

public class DefaultComparator<T> implements Comparator<T>{

    private final String[] properties;
    private final boolean[] asc;
    private final boolean nullValueBefore;

    public DefaultComparator(String[] properties, boolean[] asc, boolean nullValueBefore) {
        this.properties = properties;
        this.asc = asc;
        this.nullValueBefore = nullValueBefore;
    }

    @Override
    public int compare(T o1, T o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }

        return _compare(o1, o2);
    }

    private int _compare(T o1, T o2) {
        int result = 0;

        for (int i = 0; i < properties.length; i++) {
            String prop = properties[i];
            Object valueProp1 = ObjectUtils.getPropertyValue(o1, prop);
            Object valueProp2 = ObjectUtils.getPropertyValue(o2, prop);

            if (valueProp1 == null && valueProp2 == null) {
                result = 0;
            }
            else if (valueProp1 == null) {
                result = nullValueBefore ? -1 : 1;
            }
            else if (valueProp2 == null) {
                result = nullValueBefore ? 1 : -1;
            }
            else {
                if (valueProp1.getClass().isAssignableFrom(valueProp2.getClass()) || valueProp2.getClass().isAssignableFrom(valueProp1.getClass())) {
                    if (valueProp1 instanceof String && valueProp2 instanceof String) {
                        result = ((String)valueProp1).compareTo((String)valueProp2);
                    }
                    else if (valueProp1 instanceof Timestamp && valueProp2 instanceof Timestamp) {
                        result = ((Timestamp)valueProp1).compareTo((Timestamp)valueProp2);
                    }
                    else {
                        result = ObjectUtils.compareTo(valueProp1, valueProp2);
                    }
                }
                else {
                    result = -1;
                }
                result = null == this.asc || this.asc[i] ? result : -result;
            }
        }

        return result;
    }
}
