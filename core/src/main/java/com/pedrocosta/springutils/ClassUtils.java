package com.pedrocosta.springutils;

import org.springframework.beans.BeanUtils;

import java.lang.annotation.Annotation;

/**
 * Utility class to handle {@link Class}.
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class ClassUtils {
    /**
     * Check if class has an annotation.
     *
     * @param clazz Class to look.
     * @return true if class has annotation, false otherwise.
     */
    public static boolean hasAnnotation(final Class<?> clazz, final Class<? extends Annotation> annotationClass) {
        boolean has = false;
        try {
            Annotation annot = clazz.getAnnotation(annotationClass);
            has = annot != null;
        } catch (NullPointerException ignored) {}
        return has;
    }

    @Deprecated
    public static boolean isPrimitive(final Class<?> clazz) {
        return WrapperType.is(clazz) || clazz.isPrimitive();
    }

    public static boolean isSimpleProperty(final Class<?> clazz) {
        return BeanUtils.isSimpleProperty(clazz);
    }

    public static boolean hasDeclaredMethod(final Class<?> clazz, final String methodName) {
        boolean has = true;
        try {
            clazz.getDeclaredMethod(methodName, Object.class);
        } catch (NoSuchMethodException e) {
            has = false;
        }
        return has;
    }
}
