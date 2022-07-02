package com.pedrocosta.springutils.viewmapper;

import com.pedrocosta.springutils.viewmapper.resolver.CollectionTypeResolver;
import com.pedrocosta.springutils.viewmapper.resolver.TypeResolver;
import com.pedrocosta.springutils.viewmapper.resolver.instance.CollectionInstanceResolverFactory;

import java.util.Collection;

public class ViewCollectionMapper extends CoreViewMapper {
    @Override
    @SuppressWarnings("unchecked")
    protected <T, F> T doMapping(F from, Class<T> resultClass) {
        if (!(from instanceof Collection)) {
            throw new IllegalArgumentException("Source is not a Collection");
        }

        Collection<?> col = (Collection<?>) from;
        T result = (T) CollectionInstanceResolverFactory.create(from.getClass()).newInstance(from.getClass());

        if (col.isEmpty()) {
            return result;
        }

        //Do the mapping of elements
        if (result != null) {
            for (Object obj : col) {
                ((Collection<Object>) result).add(loadMapper(resultClass).map(obj, resultClass));
            }
        }

        return result;
    }

    @Override
    protected TypeResolver getTypeResolver() {
        return new CollectionTypeResolver();
    }
}
