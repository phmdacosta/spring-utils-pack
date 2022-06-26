package com.pedrocosta.springutils.viewmapper;

import com.pedrocosta.springutils.WrapperType;

public class ViewWrapperMapper extends CoreViewMapper {
    @Override
    @SuppressWarnings("unchecked")
    protected <T, F> T doMapping(F from, Class<T> resultClass) {
        if (!WrapperType.is(resultClass)) {
            return loadMapper(resultClass).map(from, resultClass);
        }
        return (T) from;
    }
}
