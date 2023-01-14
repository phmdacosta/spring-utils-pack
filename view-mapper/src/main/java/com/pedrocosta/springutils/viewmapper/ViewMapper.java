package com.pedrocosta.springutils.viewmapper;

import com.pedrocosta.springutils.viewmapper.builder.ViewMapperBuilder;

import java.util.Collection;

public class ViewMapper {

    private ViewMapperBuilder builder;

    public ViewMapper() {
        this.builder = new ViewMapperBuilder();
    }

    public ViewMapperBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(ViewMapperBuilder builder) {
        this.builder = builder;
    }

    /**
     * Map a model to a view, and vice versa.
     *
     * @param from      Object to map
     * @param toClass   Class to the target result
     * @param <FROM> Type of the object to map
     * @param <TO>   Type of the result
     */
    @SuppressWarnings("unchecked")
    public <FROM, TO> TO map(FROM from, Class<TO> toClass) {
        TypeMapper<FROM, TO> mapper = (TypeMapper<FROM, TO>) new ViewMapperBuilder().create(from.getClass(), toClass);
        return mapper.doMapping(from, toClass);
    }

    @SuppressWarnings("unchecked")
    public <ELEM, FROM extends Collection<ELEM>, TO> Collection<TO> map(FROM from, Class<TO> toClass) {
        CollectionMapper<FROM, TO> mapper = (CollectionMapper<FROM, TO>) new ViewMapperBuilder().create(from.getClass(), Collection.class);
        return (Collection<TO>) mapper.doMapping(from, toClass);
    }
}
