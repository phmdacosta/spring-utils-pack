package com.pedrocosta.springutils.viewmapper.validator;

import java.util.Map;

public class MapperMapValidator implements MapperTypeValidator {
    @Override
    public boolean accept(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }
}
