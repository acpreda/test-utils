package com.acpreda.test.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ReflectionAssertionsTest {

    @Test
    @DisplayName("Should accept annotated classes")
    void should_accept_annotated_classes() {
        ReflectionAssertions.assertIsAnnotated(AnnotatedClass.class, TestingAnnotation.class);
    }

    @Test
    @DisplayName("Should reject not annotated classes")
    void should_reject_not_annotated_classes() {
        assertThrows(
                Throwable.class,
                () -> ReflectionAssertions.assertIsAnnotated(
                        NotAnnotatedClass.class,
                        TestingAnnotation.class
                )
        );
    }

    @Test
    @DisplayName("Should accept empty class when checking for default constructor")
    void should_accept_empty_class_when_checking_for_default_constructor() {
        ReflectionAssertions.assertHasDefaultConstructor(EmptyClass.class);
    }

    @Test
    @DisplayName("Should accept empty class when checking for POJO")
    void should_accept_empty_class_when_checking_for_pojo() {
        ReflectionAssertions.assertIsPojo(EmptyClass.class);
    }

    @Test
    @DisplayName("Should fail when class does not met POJO pattern")
    void should_fail_when_class_does_not_met_POJO_pattern() {
        assertThrows(Throwable.class, () -> ReflectionAssertions.assertIsPojo(NoGetNoSetClass.class));
        assertThrows(Throwable.class, () -> ReflectionAssertions.assertIsPojo(NoSetClass.class));
        assertThrows(Throwable.class, () -> ReflectionAssertions.assertIsPojo(NoGetClass.class));
        assertThrows(Throwable.class, () -> ReflectionAssertions.assertIsPojo(NoFieldSupportClass.class));
    }

    @Test
    @DisplayName("Should accept when method is present")
    void should_accept_when_method_is_present() {
        ReflectionAssertions.assertHasMethod(NoFieldSupportClass.class, "getAttrib");
        ReflectionAssertions.assertHasMethod(NoFieldSupportClass.class, "setAttrib", String.class);
    }

    @Test
    @DisplayName("Should reject when method is not present")
    void should_reject_when_method_is_not_present() {
        assertThrows(Throwable.class, () ->
                ReflectionAssertions.assertHasMethod(NoFieldSupportClass.class, "betAttrib"));
        assertThrows(Throwable.class, () ->
                ReflectionAssertions.assertHasMethod(NoFieldSupportClass.class, "setAttrib"));
    }

    @Test
    @DisplayName("Should reject when class constructor throws exception")
    void should_reject_when_class_constructor_throws_exception() {
        assertThrows(Throwable.class, () -> ReflectionAssertions.assertIsPojo(ConstThrowsException.class));
    }


}