package com.pedrocosta.springutils.viewmapper.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface View {
    Class<Object> modelClass = null;
}
