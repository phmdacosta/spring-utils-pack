package com.pedrocosta.springutils.viewmapper;

import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.viewmapper.resolver.MapTypeResolver;
import com.pedrocosta.springutils.viewmapper.resolver.TypeResolver;

import java.util.HashMap;
import java.util.Map;

public class MapMapper<FROM, TO> extends TypeMapper<FROM, TO> {
    private Class<TO> resultClass;

    @Override
    protected TO map(FROM from, Class<TO> resultClass) {
        if (!(from instanceof Map)) {
            throw new IllegalArgumentException("Source object is not a Map");
        }
        this.resultClass = resultClass;
        return map(from);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected TO map(FROM from) {
        Map<?,?> map = (Map<?,?>) from;
        TO result;
        try { //Try to create
            result = (TO) map.getClass().getDeclaredConstructor(new Class[0]).newInstance();
            ((Map<Object,Object>) result).clear();
        } catch (Exception e) {
            Log.error(this, e);
            result = (TO) new HashMap<>();
        }

        if (map.isEmpty()) {
            return result;
        }

        //Do the mapping of elements
        for (Map.Entry<?,?> entry : map.entrySet()) {
            ((Map<Object,Object>) result)
                    .put(entry.getKey(), ((TypeMapper<Object,TO>)loadMapper(entry.getValue().getClass(), resultClass))
                            .doMapping(entry.getValue(), resultClass));
        }

        return result;
    }

    @Override
    protected TypeResolver getTypeResolver() {
        return new MapTypeResolver();
    }
}
