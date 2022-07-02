package com.pedrocosta.springutils.viewmapper.resolver.instance;

import com.pedrocosta.springutils.viewmapper.MapperUtils;

public class DefaultInstanceResolver implements CollectionInstanceResolver<Object> {
    @Override
    public Object newInstance(Class<?> clazz) {
        return MapperUtils.getInstance(clazz);
    }
}
