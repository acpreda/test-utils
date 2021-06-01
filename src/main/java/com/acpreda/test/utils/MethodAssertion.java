package com.acpreda.test.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MethodAssertion {

    private final Method subject;

    private MethodAssertion(Method subject) {
        this.subject = subject;
    }

    public static MethodAssertion of(Method method) {
        return new MethodAssertion(method);
    }

    public AnnotationAssertion isAnnotated(Class<? extends Annotation> annotation) {
        Annotation extracted = subject.getAnnotation(annotation);
        assertNotNull(extracted);
        return AnnotationAssertion.of(extracted);
    }

}
