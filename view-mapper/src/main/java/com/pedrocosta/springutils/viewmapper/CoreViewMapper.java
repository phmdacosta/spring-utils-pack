package com.pedrocosta.springutils.viewmapper;

import com.pedrocosta.springutils.viewmapper.builder.ViewMapperBuilder;
import com.pedrocosta.springutils.viewmapper.resolver.DefaultTypeResolver;
import com.pedrocosta.springutils.viewmapper.resolver.TypeResolver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class CoreViewMapper {

    private ViewMapperBuilder mapperBuilder;

    public CoreViewMapper setMapperBuilder(ViewMapperBuilder mapperBuilder) {
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
     * @param <T>   Type of the result
     * @param <F>   Type of the object to map
     */
    @SuppressWarnings("unchecked")
    public <T,F> T map(F from, Class<T> toClass) {
        if (!this.isValidParameters(from, toClass)) {
            return null;
        }
        if (from.getClass().equals(toClass) && from instanceof Cloneable) {
            try { // Try to clone, if can't do the mapping
                T cloned = (T) from.getClass().getDeclaredMethod("clone", new Class[0]).invoke(from);
                if (cloned != null) return cloned;
            } catch (Exception ignored) {}
        }
        return this.doMapping(from, toClass);
    }

    /**
     * Map an object into a field.
     * <br>
     * Field type must be a relationship model <> view
     * with <code>from</code> parameter.
     *
     * @param from      Object to map
     * @param toField   Field to save result
     * @param <T>   Type of the result
     * @param <F>   Type of the object to map
     */
    @SuppressWarnings("unchecked")
    public <T,F> T map(F from, Field toField) {
        if (!this.isValidParameters(from, toField)) {
            return null;
        }
        Class<T> toClass = (Class<T>) this.getTypeResolver().getType(toField);
        return this.map(from, toClass);
    }

    /**
     * Map an object into a method.
     * <br>
     * Method type must be a relationship model <> view
     * with <code>from</code> parameter.
     *
     * @param from      Object to map
     * @param toMethod  Method to save result
     * @param <T>   Type of the result
     * @param <F>   Type of the object to map
     */
    @SuppressWarnings("unchecked")
    public <T,F> T map(F from, Method toMethod) {
        if (!this.isValidParameters(from, toMethod)) {
            return null;
        }
        Class<T> toClass = (Class<T>) this.getTypeResolver().getType(toMethod);
        return this.map(from, toClass);
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

    protected CoreViewMapper loadMapper(Field field) {
        return loadMapper(MapperUtils.getTypeOfField(field));
    }

    protected CoreViewMapper loadMapper(Method method) {
        return loadMapper(MapperUtils.getTypeOfMethod(method));
    }

    protected <T> CoreViewMapper loadMapper(Class<T> clazz) {
        if (this.getMapperBuilder() == null)
            this.setMapperBuilder(new ViewMapperBuilder());

        return this.getMapperBuilder().create(clazz);
    }

    protected TypeResolver getTypeResolver() {
        return new DefaultTypeResolver();
    }

    protected abstract <T,F> T doMapping(F from, Class<T> resultClass);
}
