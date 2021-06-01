package com.acpreda.test.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.fail;

public class AnnotationAssertion {

    private final Annotation subject;

    public AnnotationAssertion(Annotation subject) {
        this.subject = subject;
    }

    public static AnnotationAssertion of(Annotation subject) {
        return new AnnotationAssertion(subject);
    }

    public Annotation getSubject() {
        return subject;
    }

    public AnnotationMethodAssertion with(String attributeName) {
        try {
            Method method = subject.getClass().getDeclaredMethod(attributeName);
            return AnnotationMethodAssertion.of(subject, method);
        } catch (NoSuchMethodException e) {
            fail("Field " + attributeName + " not found in annotation " + subject.getClass().getName());
        }
        return null;
    }
}
