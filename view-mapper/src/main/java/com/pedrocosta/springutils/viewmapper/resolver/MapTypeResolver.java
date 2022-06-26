package com.pedrocosta.springutils.viewmapper.resolver;

import com.pedrocosta.springutils.viewmapper.MapperUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class MapTypeResolver implements TypeResolver {
    @Override
    public Class<?> getType(Method method) {
        return MapperUtils.getGenericTypesOfMethod(method)[1];
    }

    @Override
    public Class<?> getType(Field field) {
        return MapperUtils.getGenericTypesOfField(field)[1];
    }
}
