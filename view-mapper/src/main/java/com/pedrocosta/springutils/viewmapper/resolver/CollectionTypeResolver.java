package com.pedrocosta.springutils.viewmapper.resolver;

import com.pedrocosta.springutils.viewmapper.MapperUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CollectionTypeResolver implements TypeResolver {
    @Override
    public Class<?> getType(Method method) {
        return MapperUtils.getGenericTypesOfMethod(method)[0];
    }

    @Override
    public Class<?> getType(Field field) {
        return MapperUtils.getGenericTypesOfField(field)[0];
    }
}
