package com.pedrocosta.springutils.viewmapper.validator;

public class MapperModelValidator implements MapperTypeValidator {
    @Override
    public boolean accept(Class<?> clazz) {
        return false;
    }
}
