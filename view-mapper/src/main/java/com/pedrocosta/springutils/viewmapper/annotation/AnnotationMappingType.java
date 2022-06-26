package com.pedrocosta.springutils.viewmapper.annotation;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public enum AnnotationMappingType {
    FIELD(MappingField.class),
    COLLECTION(MappingCollection.class),
    MAP(MappingMap.class);

    final Class<? extends Annotation> annotationClass;

    AnnotationMappingType(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public static boolean has(Class<?> clazz) {
        return Arrays.stream(AnnotationMappingType.values())
                .filter(type -> type.annotationClass.equals(clazz))
                .findAny().orElse(null) != null;
    }

    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }
}
