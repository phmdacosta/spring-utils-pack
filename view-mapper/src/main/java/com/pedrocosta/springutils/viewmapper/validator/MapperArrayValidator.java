package com.pedrocosta.springutils.viewmapper.validator;

public class MapperArrayValidator implements MapperTypeValidator {
    @Override
    public boolean accept(Class<?> clazz) {
        return clazz != null && clazz.isArray();
    }
}
