package com.pedrocosta.springutils.viewmapper.validator;

public class MapperObjectValidator implements MapperTypeValidator {
    @Override
    public boolean accept(Class<?> clazz) {
        return false;
    }
}
