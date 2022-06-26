package com.pedrocosta.springutils.viewmapper;

import com.pedrocosta.springutils.ClassUtils;
import com.pedrocosta.springutils.output.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class ViewModelMapper extends CoreViewMapper {

    protected <T,F> T doMapping(F from, Class<T> resultClass) {
        return doMappingByMethod(from, resultClass);
    }

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
                        fieldValue = loadMapper(resultField).map(fieldValue, resultField);
                    }
                    resultSetter.invoke(result, fieldValue); // Do the set
                }
            }
        } catch (Exception e) {
            Log.warn(ViewMapper.class, e.getMessage());
        }

        return result;
    }

    protected <T,F> T doMappingByMethod(F from, Class<T> resultClass) {
        T result = null;

        try {
            Class<?> fromClass = from.getClass();
            result = MapperUtils.getInstance(resultClass);
            Map<Method, Method> mappedFields = MapperUtils.getMappedMethods(fromClass, resultClass);

            for (Map.Entry<Method, Method> entry : mappedFields.entrySet()) {
                // Retrieve setter method
                Method resultSetter = entry.getKey();
                if (resultSetter != null) {
                    // Retrieve getter from source object
                    Method fromGetter = entry.getValue();
                    Object fieldValue = fromGetter != null ? fromGetter.invoke(from) : null;

                    if (fieldValue != null && !ClassUtils.isPrimitive(fieldValue.getClass())) {
                        fieldValue = loadMapper(resultSetter).map(fieldValue, resultSetter);
                    }
                    resultSetter.invoke(result, fieldValue); // Do the set
                }
            }
        } catch (Exception e) {
            Log.warn(ViewMapper.class, e.getMessage());
        }

        return result;
    }
}
