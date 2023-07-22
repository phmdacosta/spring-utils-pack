package com.pedrocosta.springutils.test;

import com.pedrocosta.springutils.ClassFinder;
import com.pedrocosta.springutils.test.utils.AnnotationFinderTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClassFinderTest {

    @Test
    public void test_findClassesByAnnotation_withPackageParameter() throws ClassNotFoundException {
        List<Class<?>> foundList = ClassFinder.findAllByAnnotation(AnnotationFinderTest.class, this.getClass().getPackage());
        Class<?> targetClass = Class.forName("com.pedrocosta.springutils.test.utils.ModelTestAnnotation");
        assertTrue(foundList.contains(targetClass));
    }

    @Test
    public void test_findClassesByAnnotation_withoutPackageParameter() throws ClassNotFoundException {
        List<Class<?>> foundList = ClassFinder.findAllByAnnotation(AnnotationFinderTest.class);
        Class<?> targetClass = Class.forName("com.pedrocosta.springutils.test.utils.ModelTestAnnotation");
        assertTrue(foundList.contains(targetClass));
    }
}
