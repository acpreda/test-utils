package com.acpreda.test.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.fail;

public class AnnotationMethodAssertion {

    private final Annotation annotation;
    private final Method method;

    private AnnotationMethodAssertion(Annotation annotation, Method method) {
        this.annotation = annotation;
        this.method = method;
    }

    public static AnnotationMethodAssertion of(Annotation annotation, Method method) {
        return new AnnotationMethodAssertion(annotation, method);
    }

    public void assertArrayEquals(Object[] expectedArray) {
        Object annotationValue = getAnnotationValue();
        if (annotationValue != null && annotationValue.getClass().isArray()) {
            Object[] values = (Object[]) annotationValue;
            for (Object value : values) {
                boolean found = false;
                for (Object expected : expectedArray) {
                    if (value.equals(expected)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    fail("Expected array but " + annotationValue);
                }
            }
        } else {
            fail("Expected array but " + annotationValue);
        }
    }

    private Object getAnnotationValue() {
        Object annotationValue = null;
        try {
            annotationValue = method.invoke(annotation);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Annotation value could not be obtained");
        }
        return annotationValue;
    }

    public void assertEquals(Object expectedValue) {
        Object annotationValue = getAnnotationValue();
        if (expectedValue == null) {
            if (annotationValue != null) {
                fail("Expected " + expectedValue + " but " + annotationValue);
            } else {
                return;
            }
        }
        if (!annotationValue.equals(expectedValue)) {
            fail("Expected " + expectedValue + " but " + annotationValue);
        }
    }
}
