package com.pedrocosta.springutils.viewmapper;

import com.pedrocosta.springutils.viewmapper.builder.ViewMapperBuilder;
import com.pedrocosta.springutils.viewmapper.resolver.DefaultTypeResolver;
import com.pedrocosta.springutils.viewmapper.resolver.TypeResolver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class TypeMapper<FROM, TO> {

    private ViewMapperBuilder mapperBuilder;

    public TypeMapper<FROM, TO> setMapperBuilder(ViewMapperBuilder mapperBuilder) {
        this.mapperBuilder = mapperBuilder;
        return this;
    }

    public ViewMapperBuilder getMapperBuilder() {
        return mapperBuilder;
    }

    /**
     * Map a model to a view, and vice versa.
     *
     * @param from      Object to map
     * @param toClass   Class to the target result
     */
    public TO doMapping(FROM from, Class<TO> toClass) {
        if (!this.isValidParameters(from, toClass)) {
            return null;
        }
        return this.map(from, toClass);
    }

    /**
     * Map an object into a field.
     * <br>
     * Field type must be a relationship model <> view
     * with <code>from</code> parameter.
     *
     * @param from      Object to map
     * @param toField   Field to save result
     */
    @SuppressWarnings("unchecked")
    public TO doMapping(FROM from, Field toField) {
        if (!this.isValidParameters(from, toField)) {
            return null;
        }
        Class<TO> toClass = (Class<TO>) this.getTypeResolver().getType(toField);
        return this.doMapping(from, toClass);
    }

    /**
     * Map an object into a method.
     * <br>
     * Method type must be a relationship model <> view
     * with <code>from</code> parameter.
     *
     * @param from      Object to map
     * @param toMethod  Method to save result
     */
    @SuppressWarnings("unchecked")
    public TO doMapping(FROM from, Method toMethod) {
        if (!this.isValidParameters(from, toMethod)) {
            return null;
        }
        Class<TO> toClass = (Class<TO>) this.getTypeResolver().getType(toMethod);
        return this.doMapping(from, toClass);
    }

    protected <T,F> boolean isValidParameters(F from, Class<T> clazz) {
        return from != null && clazz != null;
    }

    protected <F> boolean isValidParameters(F from, Field field) {
        return from != null && field != null;
    }

    protected <F> boolean isValidParameters(F from, Method method) {
        return from != null && method != null;
    }

    protected TypeMapper<?,?> loadMapper(Class<?> fromClass, Field field) {
        return loadMapper(fromClass, MapperUtils.getTypeOfField(field));
    }

    protected TypeMapper<?,?> loadMapper(Class<?> fromClass, Method method) {
        return loadMapper(fromClass, MapperUtils.getTypeOfMethod(method));
    }

    protected TypeMapper<?,?> loadMapper(Class<?> fromClass, Class<?> toClass) {
        if (this.getMapperBuilder() == null)
            this.setMapperBuilder(new ViewMapperBuilder());

        return this.getMapperBuilder().create(fromClass, toClass);
    }

    protected TypeResolver getTypeResolver() {
        return new DefaultTypeResolver();
    }

    protected abstract TO map(FROM from, Class<TO> resultClass);
}
