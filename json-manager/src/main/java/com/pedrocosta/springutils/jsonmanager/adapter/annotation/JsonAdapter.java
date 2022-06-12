package com.pedrocosta.springutils.jsonmanager.adapter.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonAdapter {
    Class<?> type();
}
