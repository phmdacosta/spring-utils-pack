package com.pedrocosta.springutils.viewmapper.builder;

import com.pedrocosta.springutils.viewmapper.*;
import com.pedrocosta.springutils.viewmapper.validator.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ViewMapperBuilder {

    public <T> CoreViewMapper create(Class<T> clazz) {
        return MapperType.get(clazz).getMapper().setMapperBuilder(this);
    }

    private enum MapperType {
        MODEL(new MapperModelValidator(), new ViewModelMapper()),
        COLLECTION(new MapperCollectionValidator(), new ViewCollectionMapper()),
        MAP(new MapperMapValidator(), new ViewMapMapper()),
        ARRAY(new MapperArrayValidator(), new ViewArrayMapper()),
        WRAPPER(new MapperWrapperValidator(), new ViewWrapperMapper());

        final MapperTypeValidator validator;
        final CoreViewMapper mapper;

        MapperType(MapperTypeValidator validator, CoreViewMapper mapper) {
            this.validator = validator;
            this.mapper = mapper;
        }

        public static MapperType get(Class<?> clazz) {
            return Arrays.stream(ViewMapperBuilder.MapperType.values())
                    .filter(type -> type.validator.accept(clazz))
                    .findAny().orElse(MODEL);
        }

        public static MapperType get(Field field) {
            return MapperType.get(field.getType());
        }

        public static MapperType get(Method method) {
            return MapperType.get(MapperUtils.getTypeOfMethod(method));
        }

        public CoreViewMapper getMapper() {
            return mapper;
        }
    }
}
