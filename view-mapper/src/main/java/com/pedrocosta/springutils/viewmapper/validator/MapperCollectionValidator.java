package com.pedrocosta.springutils.viewmapper.validator;

import java.util.Collection;

public class MapperCollectionValidator implements MapperTypeValidator {
    @Override
    public boolean accept(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }
}
