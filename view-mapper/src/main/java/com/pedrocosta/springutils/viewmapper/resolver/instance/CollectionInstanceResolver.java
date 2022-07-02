package com.pedrocosta.springutils.viewmapper.resolver.instance;

public interface CollectionInstanceResolver<T> {
    T newInstance(Class<?> clazz);
}
