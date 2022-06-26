package com.pedrocosta.springutils.viewmapper;

import com.pedrocosta.springutils.output.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;

public class ViewArrayMapper extends CoreViewMapper {
    @Override
    protected <T, F> T doMapping(F from, Class<T> resultElementClass) {
        if (!from.getClass().isArray()) {
            throw new IllegalArgumentException(
                    String.format("%s is not a array", from.getClass().getSimpleName()));
        }

        Object[] array = (Object[]) from;
        return new ViewCollectionMapper().doMapping(Arrays.asList(array), resultElementClass);
    }
}
