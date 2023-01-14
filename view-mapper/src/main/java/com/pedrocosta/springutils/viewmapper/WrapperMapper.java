package com.pedrocosta.springutils.viewmapper;

import com.pedrocosta.springutils.WrapperType;

public class WrapperMapper<FROM, TO> extends TypeMapper<FROM, TO> {

    @SuppressWarnings("unchecked")
    protected TO map(FROM from, Class<TO> resultClass) {
        if (!WrapperType.is(resultClass)) {
            return ((TypeMapper<FROM, TO>) loadMapper(from.getClass(), resultClass)).doMapping(from, resultClass);
        }
        return null;
    }
}
