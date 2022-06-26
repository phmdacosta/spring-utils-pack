package com.pedrocosta.springutils.viewmapper.resolver;

import com.pedrocosta.springutils.viewmapper.MapperUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DefaultTypeResolver implements TypeResolver {
    @Override
    public Class<?> getType(Method method) {
        return MapperUtils.getTypeOfMethod(method);
    }

    @Override
    public Class<?> getType(Field field) {
        return MapperUtils.getTypeOfField(field);
    }
}
