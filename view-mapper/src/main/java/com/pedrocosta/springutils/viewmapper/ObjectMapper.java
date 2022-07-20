package com.pedrocosta.springutils.viewmapper;

import com.pedrocosta.springutils.ClassUtils;
import com.pedrocosta.springutils.output.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class ObjectMapper extends TypeMapper<Object, Object> {

    private Class<Object> resultClass;

    protected Object map(Object from, Class<Object> resultClass) {
        this.resultClass = resultClass;
        return map(from);
    }

    protected Object map(Object from) {
        Object result = null;
        try {
            result = MapperUtils.getInstance(resultClass);
            Class<?> fromClass = from.getClass();
            doMapping(from, fromClass, result, resultClass);
        } catch (Exception e) {
            Log.warn(ViewMapper.class, e.getMessage());
        }

        return result;
    }

    protected <T,F> void doMapping(F from, Class<?> fromClass, T result, Class<?> resultClass) {
        doMappingByMethod(from, fromClass, result, resultClass);
    }

    @SuppressWarnings("unchecked")
    protected <T,F> T doMappingByField(F from, Class<T> resultClass) {
        T result = null;

        try {
            Class<?> fromClass = from.getClass();
            result = MapperUtils.getInstance(resultClass);
            Map<Field, Field> mappedFields = MapperUtils.getMappedFields(fromClass, resultClass);

            for (Map.Entry<Field, Field> entry : mappedFields.entrySet()) {
                Field resultField = entry.getKey();
                Field fromField = entry.getValue();
                // Retrieve setter method
                Method resultSetter = MapperUtils.setter(resultClass, resultField);
                if (resultSetter != null) {
                    // Retrieve getter from source object
                    Method fromGetter = MapperUtils.getter(fromClass, fromField);
                    Object fieldValue = fromGetter != null ? fromGetter.invoke(from) : null;

                    if (fieldValue != null && !ClassUtils.isPrimitive(fieldValue.getClass())) {
                        fieldValue = ((TypeMapper<Object, Object>)loadMapper(fromClass, resultField)).doMapping(fieldValue, resultField);
                    }
                    resultSetter.invoke(result, fieldValue); // Do the set
                }
            }
        } catch (Exception e) {
            Log.warn(ViewMapper.class, e.getMessage());
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private <T, F> void doMappingByMethod(F from, Class<?> fromClass, T result, Class<?> resultClass) {
        Map<Method, Method> mappedMethods = MapperUtils.getMappedMethods(this, fromClass, resultClass);

        for (Map.Entry<Method, Method> entry : mappedMethods.entrySet()) {
            // Retrieve setter method
            Method resultSetter = entry.getKey();
            if (resultSetter != null) {
                try {
                    // Retrieve getter from source object
                    Method fromGetter = entry.getValue();
                    Object fieldValue = fromGetter != null ? fromGetter.invoke(from) : null;

                    if (fieldValue != null && !ClassUtils.isPrimitive(fieldValue.getClass())) {
                        fieldValue = ((TypeMapper<Object, Object>)loadMapper(fromClass, resultSetter)).doMapping(fieldValue, resultSetter);
                    }

                    resultSetter.invoke(result, fieldValue); // Do the set
                } catch (Exception e) {
                    Log.warn(ViewMapper.class, e.getMessage());
                }
            }
        }
    }
}
