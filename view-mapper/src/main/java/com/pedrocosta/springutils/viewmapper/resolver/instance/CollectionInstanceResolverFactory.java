package com.pedrocosta.springutils.viewmapper.resolver.instance;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class CollectionInstanceResolverFactory {

    public static CollectionInstanceResolver<?> create(Class<?> clazz) {
        InstanceResolverType type = InstanceResolverType.get(clazz);
        return type.getInstanceResolver();
    }

    private enum InstanceResolverType {
        DEFAULT(Object.class, new DefaultInstanceResolver()),
        SET(Set.class, new SetInstanceResolver<>()),
        LIST(List.class, new ListInstanceResolver<>());

        private final Class<?> clazz;
        private final CollectionInstanceResolver<?> resolver;

        private InstanceResolverType(Class<?> clazz, CollectionInstanceResolver<?> resolver) {
            this.clazz = clazz;
            this.resolver = resolver;
        }

        public static InstanceResolverType get(Class<?> clazz) {
            return Arrays.stream(InstanceResolverType.values())
                    .filter(type ->
                            !type.equals(DEFAULT) && Arrays.stream(clazz.getInterfaces())
                                    .anyMatch(interfaceClass -> interfaceClass.isAssignableFrom(type.clazz)))
                    .findAny().orElse(DEFAULT);
        }

        public CollectionInstanceResolver<?> getInstanceResolver() {
            return this.resolver;
        }
    }
}
