package com.pedrocosta.springutils.viewmapper.builder;

import com.pedrocosta.springutils.viewmapper.*;
import com.pedrocosta.springutils.viewmapper.validator.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class ViewMapperBuilder {

    private final Map<MapperKey<?,?>, TypeMapper<?,?>> mappers;

    public ViewMapperBuilder() {
        mappers = new HashMap<>();
    }

    public ViewMapperBuilder addMapper(TypeMapper<?,?> mapper, Class<?> fromClass, Class<?> toClass) {
        MapperKey<?,?> key = new MapperKey<>(fromClass, toClass);
        this.mappers.put(key, mapper);
        return this;
    }

    public TypeMapper<?,?> create(Class<?> fromClass, Class<?> toClass) {
        TypeMapper<?,?> typeMapper = findMapper(fromClass, toClass);
        return typeMapper != null ? typeMapper : create(toClass);
    }

    public TypeMapper<?,?> create(Class<?> clazz) {
        return MapperType.get(clazz).getMapper().setMapperBuilder(this);
    }

    private TypeMapper<?,?> findMapper(Class<?> fromClass, Class<?> toClass) {
        Map.Entry<MapperKey<?,?>, TypeMapper<?,?>> entry = this.mappers.entrySet().stream().filter(
                e -> new MapperKey<>(fromClass, toClass).equals(e.getKey())
        ).findAny().orElse(null);
        if (entry != null) {
            return entry.getValue();
        }
        return null;
    }

    private enum MapperType {
        DEFAULT(new MapperObjectValidator(), new ObjectMapper()),
        @SuppressWarnings("rawtypes")
        COLLECTION(new MapperCollectionValidator(), new CollectionMapper()),
        @SuppressWarnings("rawtypes")
        MAP(new MapperMapValidator(), new MapMapper()),
        @SuppressWarnings("rawtypes")
        ARRAY(new MapperArrayValidator(), new ArrayMapper()),
        @SuppressWarnings("rawtypes")
        WRAPPER(new MapperWrapperValidator(), new WrapperMapper());

        final MapperTypeValidator validator;
        final TypeMapper<?,?> mapper;

        MapperType(MapperTypeValidator validator, TypeMapper<?,?> mapper) {
            this.validator = validator;
            this.mapper = mapper;
        }

        public static MapperType get(Class<?> clazz) {
            return Arrays.stream(ViewMapperBuilder.MapperType.values())
                    .filter(type -> type.validator.accept(clazz))
                    .findAny().orElse(DEFAULT);
        }

        public static MapperType get(Field field) {
            return MapperType.get(field.getType());
        }

        public static MapperType get(Method method) {
            return MapperType.get(MapperUtils.getTypeOfMethod(method));
        }

        public TypeMapper<?,?> getMapper() {
            return mapper;
        }
    }
}
