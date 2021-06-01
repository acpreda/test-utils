package com.acpreda.test.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FieldAssertion {

    private final Field subject;

    private FieldAssertion(Field subject) {
        this.subject = subject;
    }

    public static FieldAssertion of(Field field) {
        return new FieldAssertion(field);
    }

    public AnnotationAssertion isAnnotated(Class<? extends Annotation> annotation) {
        Annotation extracted = subject.getAnnotation(annotation);
        assertNotNull(extracted);
        return AnnotationAssertion.of(extracted);
    }
}
