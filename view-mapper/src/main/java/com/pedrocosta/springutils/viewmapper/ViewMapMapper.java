package com.pedrocosta.springutils.viewmapper;

import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.viewmapper.resolver.MapTypeResolver;
import com.pedrocosta.springutils.viewmapper.resolver.TypeResolver;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ViewMapMapper extends CoreViewMapper {
    @Override
    @SuppressWarnings("unchecked")
    protected <T, F> T doMapping(F from, Class<T> resultClass) {
        if (!(from instanceof Map<?,?> map)) {
            throw new IllegalArgumentException("Source object is not a Map");
        }

        T result = null;
        try { //Try to create
            result = (T) map.getClass().getDeclaredConstructor(new Class[0]).newInstance();
        } catch (InstantiationException | NoSuchMethodException |
                 InvocationTargetException | IllegalAccessException e) {
            Log.error(this, e);
        }

        if (map.isEmpty()) {
            return result;
        }

        //Do the mapping of elements
        if (result != null) {
            for (Map.Entry<?,?> entry : map.entrySet()) {
                ((Map<Object,Object>) result)
                        .put(entry.getKey(), loadMapper(resultClass).map(entry.getValue(), resultClass));
            }
        }

        return result;
    }

    @Override
    protected TypeResolver getTypeResolver() {
        return new MapTypeResolver();
    }
}
