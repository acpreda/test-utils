package com.acpreda.test.utils;

import com.acpreda.test.utils.samples.AnnotatedClass;
import com.acpreda.test.utils.samples.TestingAnnotation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationAssertionTest {

    AnnotationAssertion annotationAssertion;
    Annotation annotation;

    @BeforeEach
    void beforeEach() {
        annotation = AnnotatedClass.class.getAnnotation(TestingAnnotation.class);
        this.annotationAssertion = AnnotationAssertion.of(annotation);
    }

    @DisplayName("Should return the subject")
    @Test
    void should_return_the_subject() {
        assertEquals(annotation, annotationAssertion.getSubject());
    }

    @DisplayName("Should pass when annotation has a method")
    @Test
    void should_pass_when_annotation_has_a_method() {
        assertNotNull(this.annotationAssertion.with("value"));
        assertNotNull(this.annotationAssertion.with("arrayValue"));
    }

    @DisplayName("Should fail when annotation does not have a method")
    @Test
    void should_fail_when_annotation_does_not_have_a_method() {
        assertThrows(Throwable.class, () -> this.annotationAssertion.with("notAMethod"));
    }

}