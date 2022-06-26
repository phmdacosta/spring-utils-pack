package com.pedrocosta.springutils.viewmapper;

import com.pedrocosta.springutils.viewmapper.builder.ViewMapperBuilder;

public interface ViewMapper {
    /**
     * Map a model to a view, and vice versa.
     *
     * @param from      Object to map
     * @param toClass   Class to the target result
     * @param <T>   Type of the result
     * @param <F>   Type of the object to map
     */
    public static <T,F> T map(F from, Class<T> toClass) {
        return new ViewMapperBuilder().create(toClass).map(from, toClass);
    }
}
