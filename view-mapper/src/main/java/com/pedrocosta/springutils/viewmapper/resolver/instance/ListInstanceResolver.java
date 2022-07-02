package com.pedrocosta.springutils.viewmapper.resolver.instance;

import java.util.*;

public class ListInstanceResolver<T> implements CollectionInstanceResolver<List<T>> {
    @Override
    @SuppressWarnings("unchecked")
    public List<T> newInstance(Class<?> clazz) {
        Class<?> listClass = Arrays.stream(clazz.getInterfaces())
                .filter(interfaceClass -> interfaceClass.isAssignableFrom(List.class))
                .findFirst().orElse(null);

        if (listClass == null) {
            return null;
        }

        List<T> obj;
        try {
            obj = (List<T>) clazz.getDeclaredConstructor(new Class[0]).newInstance();
            obj.clear();
        } catch (Exception e) {
            obj = new ArrayList<>();
        }

        return obj;
    }
}
