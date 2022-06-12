package com.pedrocosta.springutils;

import java.lang.annotation.Annotation;

public class ClassUtils {
    /**
     * Check if class has an annotation.
     *
     * @param clazz Class to look.
     * @return true if class has annotation, false otherwise.
     */
    public static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        boolean has = false;
        try {
            Annotation annot = clazz.getAnnotation(annotationClass);
            has = annot != null;
        } catch (NullPointerException ignored) {}
        return has;
    }
}
