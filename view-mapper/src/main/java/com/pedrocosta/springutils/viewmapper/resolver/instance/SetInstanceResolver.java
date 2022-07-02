package com.pedrocosta.springutils.viewmapper.resolver.instance;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SetInstanceResolver<T> implements CollectionInstanceResolver<Set<T>> {
    @Override
    @SuppressWarnings("unchecked")
    public Set<T> newInstance(Class<?> clazz) {
        Class<?> setClass = Arrays.stream(clazz.getInterfaces())
                .filter(interfaceClass -> interfaceClass.isAssignableFrom(Set.class))
                .findFirst().orElse(null);

        if (setClass == null) {
            return null;
        }

        Set<T> obj;
        try {
            obj = (Set<T>) clazz.getDeclaredConstructor(new Class[0]).newInstance();
            obj.clear();
        } catch (Exception e) {
            obj = new HashSet<>();
        }

        return obj;
    }
}
