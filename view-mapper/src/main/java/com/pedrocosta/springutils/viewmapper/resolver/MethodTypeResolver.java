package com.pedrocosta.springutils.viewmapper.resolver;

import java.lang.reflect.Method;

public interface MethodTypeResolver {
    Class<?> getType(Method method);
}
