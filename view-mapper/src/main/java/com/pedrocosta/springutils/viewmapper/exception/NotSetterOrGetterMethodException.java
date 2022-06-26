package com.pedrocosta.springutils.viewmapper.exception;

import java.lang.reflect.Method;

public class NotSetterOrGetterMethodException extends RuntimeException {
    public NotSetterOrGetterMethodException(Method method) {
        super(String.format("Method %s is not setter or getter", method != null ? method.getName() : "null"));
    }
}
