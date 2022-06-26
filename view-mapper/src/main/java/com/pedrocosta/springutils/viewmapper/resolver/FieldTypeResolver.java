package com.pedrocosta.springutils.viewmapper.resolver;

import java.lang.reflect.Field;

public interface FieldTypeResolver {
    Class<?> getType(Field field);
}
