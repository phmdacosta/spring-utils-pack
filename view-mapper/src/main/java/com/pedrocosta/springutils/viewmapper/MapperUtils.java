package com.pedrocosta.springutils.viewmapper;

import com.pedrocosta.springutils.ClassUtils;
import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.viewmapper.ViewMapper;
import com.pedrocosta.springutils.viewmapper.annotation.*;
import com.pedrocosta.springutils.viewmapper.exception.NotSetterOrGetterMethodException;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class MapperUtils {
    public static <T> T getInstance(final Class<T> clazz) {
        T result = null;
        try {
            result = clazz.getConstructor(new Class[0]).newInstance();
        } catch (NoSuchMethodException e) {
            Log.warn(ViewMapper.class,
                    String.format("Could not instantiate %s there is no default constructor",
                            clazz.getSimpleName()));
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            Log.error(ViewMapper.class, e);
        }
        return result;
    }

    public static Map<Field, Field> getMappedFields(Class<?> fromClass, Class<?> toClass) {
        Map<Field, Field> mappedFields = new HashMap<>();

        Field[] keyFields = toClass.getDeclaredFields();
        for (int i = 0; i < keyFields.length; i++) {
            Field keyField = keyFields[i];
            try {
                String valueFieldName = keyField.getName();
                if (hasViewAnnotation(toClass) && hasMappingAnnotation(keyField)) {
                    MappingField annot = keyField.getAnnotation(MappingField.class);
                    valueFieldName = annot.name();
                } else if (hasViewAnnotation(fromClass)) {
                    valueFieldName = findFieldNameWithAnnotations(fromClass, keyField.getName());
                }
                mappedFields.put(keyField, fromClass.getDeclaredField(valueFieldName));
            } catch (NoSuchFieldException ignored) {}
        }

        return mappedFields;
    }

    public static Map<Method, Method> getMappedMethods(Class<?> fromClass, Class<?> toClass) {
        Map<Method, Method> mappedFields = new HashMap<>();

        Method[] keyMethods = toClass.getDeclaredMethods();
        for (Method keyMethod : keyMethods) {
            Annotation mappingAnnot = null;
            if (hasViewAnnotation(toClass)) {
                if (hasMappingAnnotation(keyMethod))
                    mappingAnnot = getMappingAnnotation(keyMethod);
                else { // If method does not have annotation, search for its field
                    Field fieldOfMethod = getFieldOfMethod(keyMethod);
                    mappingAnnot = hasMappingAnnotation(fieldOfMethod) ? getMappingAnnotation(fieldOfMethod) : null;
                }
            }

            if (!isSetter(keyMethod)) { //Key method must be setter
                continue;
            }

            try {
                String valueFieldName = getFieldNameOfMethod(keyMethod);
                if (mappingAnnot != null) {
                    valueFieldName = getFieldNameFromMappingAnnotation(mappingAnnot);
                } else if (hasViewAnnotation(fromClass)) {
                    valueFieldName = findFieldNameWithAnnotations(fromClass, valueFieldName);
                }
                //Value method must be getter
                mappedFields.put(keyMethod, getter(fromClass, fromClass.getDeclaredField(valueFieldName)));
            } catch (NoSuchFieldException | NullPointerException ignored) {}
        }

        return mappedFields;
    }

    public static String findFieldNameWithAnnotations(Class<?> classToSearch, String fieldName) {
        if (classToSearch == null || fieldName == null) {
            //both parameters can't be null
            return null;
        }

        Field[] fields = classToSearch.getDeclaredFields();
        for (Field field : fields) {
            //Search between fields
            if (hasMappingAnnotation(field)) {
                Annotation annot = getMappingAnnotation(field);
                String annotFieldName = getFieldNameFromMappingAnnotation(annot);
                //Return found field name
                if (fieldName.equals(annotFieldName)) return field.getName();
            }
        }

        Method[] methods = classToSearch.getDeclaredMethods();
        for (Method method : methods) {
            //If no field found, search between methods
            if (isGetterOrSetter(method) && hasMappingAnnotation(method)) {
                Annotation annot = getMappingAnnotation(method);
                String annotFieldName = getFieldNameFromMappingAnnotation(annot);
                if (fieldName.equals(annotFieldName)) {
                    String fieldNameFromMethod = getFieldNameOfMethod(method);
                    //Return found field name
                    if (fieldNameFromMethod != null) return getFieldNameOfMethod(method);
                }
            }
        }

        //If not found any name, return passed field name (mapping by same name)
        return fieldName;
    }

    public static Method setter(Class<?> clazz, Field field) {
        String methodName = "set" + StringUtils.capitalize(field.getName());
        try {
            return clazz.getDeclaredMethod(methodName, field.getType());
        } catch (NoSuchMethodException ignored) {}
        return null;
    }

    public static Method getter(Class<?> clazz, Field field) {
        String getPrefix = isBooleanType(field) ? "is" : "get";
        try {
            return clazz.getDeclaredMethod(getPrefix + StringUtils.capitalize(field.getName()));
        } catch (NoSuchMethodException ignored) {}
        return null;
    }

    public static boolean hasViewAnnotation(Class<?> clazz) {
        return ClassUtils.hasAnnotation(clazz, View.class);
    }

    public static boolean hasMappingAnnotation(Field field) {
        for (AnnotationMappingType annotMapping : AnnotationMappingType.values()) {
            if (hasAnnotation(field, annotMapping.getAnnotationClass()))
                return true;
        }
        return false;
    }

    public static boolean hasMappingAnnotation(Method method) {
        for (AnnotationMappingType annotMapping : AnnotationMappingType.values()) {
            if (hasAnnotation(method, annotMapping.getAnnotationClass()))
                return true;
        }
        return false;
    }

    public static boolean hasAnnotation(Field field, Class<? extends Annotation> annotationClass) {
        return field.getAnnotation(annotationClass) != null;
    }

    public static boolean hasAnnotation(Method method, Class<? extends Annotation> annotationClass) {
        return method.getAnnotation(annotationClass) != null;
    }

    public static Annotation getMappingAnnotation(Field field) {
        for (Annotation annotation : field.getAnnotations()) {
            if (AnnotationMappingType.has(annotation.annotationType())) {
                return annotation;
            }
        }
        return null;
    }

    public static Annotation getMappingAnnotation(Method method) {
        for (Annotation annotation : method.getAnnotations()) {
            if (AnnotationMappingType.has(annotation.annotationType())) {
                return annotation;
            }
        }
        return null;
    }

    public static String getFieldNameFromMappingAnnotation(Annotation annotation) {
        if(annotation == null || !AnnotationMappingType.has(annotation.annotationType())) {
            return null;
        }
        String annotFieldName = null;
        try {
            annotFieldName = (String) annotation.annotationType()
                    .getDeclaredMethod("name", new Class[0]).invoke(annotation);
        } catch (IllegalAccessException | InvocationTargetException
                 | NoSuchMethodException | NullPointerException ignored) {}
        return annotFieldName;
    }

    public static boolean isBooleanType(Field field) {
        return field != null && boolean.class.equals(field.getGenericType());
    }

    public static boolean isBooleanType(Method method) {
        return method != null && boolean.class.equals(getTypeOfMethod(method));
    }

    public static boolean isGetterOrSetter(Method method) {
        return isGetter(method) || isSetter(method);
    }

    public static boolean isGetter(Method method) {
        if (hasAnnotation(method, Getter.class))
            return true;

        Type returnType = method.getReturnType();
        Type[] paramTypes = method.getParameterTypes();
        Field fieldOfMethod = getFieldOfMethod(method);
        return fieldOfMethod != null && !void.class.equals(returnType) && paramTypes.length == 0;
    }

    public static boolean isSetter(Method method) {
        if (hasAnnotation(method, Setter.class))
            return true;

        Type[] paramTypes = method.getParameterTypes();
        Field fieldOfMethod = getFieldOfMethod(method);
        return fieldOfMethod != null && paramTypes.length == 1;
    }

    public static String getFieldNameOfMethod(Method method) {
        Field fieldOfMethod = getFieldOfMethod(method);
        return fieldOfMethod != null ? fieldOfMethod.getName() : null;
    }

    public static Field getFieldOfMethod(Method method) {
        Class<?> classOfMethod = method.getDeclaringClass();
        Field[] fieldsOfClass = classOfMethod.getDeclaredFields();
        Field found = null;
        for (Field classField : fieldsOfClass) {
            Parameter[] methodParams = method.getParameters();
            Parameter methodParam = methodParams.length > 0 ? methodParams[0] : null;
            if (methodParam != null && classField.getName().equals(methodParam.getName())) {
                found = classField;
            } else {
                String getterPrefix = isBooleanType(method) ? "is" : "get";
                String methodPrefix = method.getName().startsWith("set") ? "set" : getterPrefix;
                String methodName = method.getName().replace(methodPrefix, "");
                found = classField.getName().equalsIgnoreCase(methodName) ? classField : null;
            }
            if (found != null) break;
        }
        return found;
    }

    public static Class<?> getTypeOfField(Field field) {
        return field.getType();
    }

    public static Class<?> getTypeOfMethod(Method method) {
        return void.class.equals(method.getReturnType()) ? method.getParameterTypes()[0] : method.getReturnType();
    }

    public static Class<?>[] getGenericTypesOfField(Field field) {
        if (hasAnnotation(field, MappingCollection.class)) {
            Class<?> elementClass = ((MappingCollection) getMappingAnnotation(field)).resultElementClass();
            if (elementClass != null) {
                return new Class[]{elementClass};
            }
        }

        if (hasAnnotation(field, MappingMap.class)) {
            Class<?> elementClass = ((MappingMap) getMappingAnnotation(field)).resultValueClass();
            if (elementClass != null) {
                return new Class[]{elementClass};
            }
        }

        Type[] types = new Type[1];
        try {
            final ParameterizedType resultMethodParamType = (ParameterizedType) field.getGenericType();
            types = resultMethodParamType.getActualTypeArguments();
        } catch (ClassCastException e) {
            types[0] = field.getGenericType();
        }

        Class<?>[] typeClasses = new Class[types.length];
        for (int i = 0; i < types.length; i++) {
            typeClasses[i] = (Class<?>) types[i];
        }
        return typeClasses;
    }

    public static Class<?>[] getGenericTypesOfMethod(Method method) {
        if (!isGetterOrSetter(method)) {
            throw new NotSetterOrGetterMethodException(method);
        }

        if (hasAnnotation(method, MappingCollection.class)) {
            Class<?> elementClass = ((MappingCollection) getMappingAnnotation(method)).resultElementClass();
            if (elementClass != null) {
                return new Class[]{elementClass};
            }
        }

        if (hasAnnotation(method, MappingMap.class)) {
            Class<?> elementClass = ((MappingMap) getMappingAnnotation(method)).resultValueClass();
            if (elementClass != null) {
                return new Class[]{elementClass};
            }
        }

        Type[] types = new Type[1];

        Type methodGenericType = method.getGenericReturnType();
        if (void.class.equals(methodGenericType)) {
            methodGenericType = method.getGenericParameterTypes()[0];
        }

        try {
            final ParameterizedType resultMethodParamType = (ParameterizedType) methodGenericType;
            types = resultMethodParamType.getActualTypeArguments();
        } catch (ClassCastException e) {
            types[0] = methodGenericType;
        }

        Class<?>[] typeClasses = new Class[types.length];
        for (int i = 0; i < types.length; i++) {
            typeClasses[i] = (Class<?>) types[i];
        }
        return typeClasses;
    }
}
