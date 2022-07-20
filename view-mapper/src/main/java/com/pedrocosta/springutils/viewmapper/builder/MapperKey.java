package com.pedrocosta.springutils.viewmapper.builder;

import java.util.Objects;

public class MapperKey<FROM, TO> {
    private final Class<FROM> fromClass;
    private final Class<TO> toClass;

    public MapperKey(Class<FROM> fromClass, Class<TO> toClass) {
        this.fromClass = fromClass;
        this.toClass = toClass;
    }

    public Class<FROM> getFromClass() {
        return fromClass;
    }

    public Class<TO> getToClass() {
        return toClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapperKey)) return false;
        MapperKey<?, ?> mapperKey = (MapperKey<?, ?>) o;
        return Objects.equals(getFromClass(), mapperKey.getFromClass()) && Objects.equals(getToClass(), mapperKey.getToClass());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFromClass(), getToClass());
    }
}
