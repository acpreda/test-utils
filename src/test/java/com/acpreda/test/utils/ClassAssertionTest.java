package com.acpreda.test.utils;

import com.acpreda.test.utils.samples.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClassAssertionTest {

    @DisplayName("Should return the subject")
    @Test
    void should_return_the_subject() {
        assertEquals(AnnotatedClass.class, ClassAssertion.of(AnnotatedClass.class).getSubject());
    }

    @DisplayName("Should pass when annotated class")
    @Test
    void should_pass_when_annotated_class() {
        ClassAssertion.of(AnnotatedClass.class).isAnnotated(TestingAnnotation.class);
    }

    @DisplayName("Should fail when not annotated class")
    @Test
    void should_fail_when_not_annotated_class() {
        assertThrows(Throwable.class, () -> ClassAssertion.of(NotAnnotatedClass.class)
                .isAnnotated(TestingAnnotation.class));
    }

    @DisplayName("Should pass when has default constructor")
    @Test
    void should_pass_when_has_default_constructor() {
        ClassAssertion.of(EmptyClass.class).hasDefaultConstructor();
    }

    @DisplayName("Should fail when does not have default constructor")
    @Test
    void should_fail_when_does_not_have_default_constructor() {
        assertThrows(Throwable.class, () ->
                ClassAssertion.of(NoDefaultConstructorClass.class).hasDefaultConstructor());
    }

    @DisplayName("Should pass POJO testing on EmptyClass")
    @Test
    void should_pass_pojo_testing_on_emptyclass() {
        ClassAssertion.of(EmptyClass.class).isPojo();
    }

    @Test
    @DisplayName("Should fail when class does not met POJO pattern")
    void should_fail_when_class_does_not_met_POJO_pattern() {
        assertThrows(Throwable.class, () -> ClassAssertion.of(NoGetNoSetClass.class).isPojo());
        assertThrows(Throwable.class, () -> ClassAssertion.of(NoSetClass.class).isPojo());
        assertThrows(Throwable.class, () -> ClassAssertion.of(NoGetClass.class).isPojo());
        assertThrows(Throwable.class, () -> ClassAssertion.of(NoFieldSupportClass.class).isPojo());
    }

    @DisplayName("Should accept when method exists")
    @Test
    void should_accept_when_method_exists() {
        ClassAssertion.of(NoFieldSupportClass.class).method("getAttrib");
        ClassAssertion.of(NoFieldSupportClass.class).method("setAttrib", String.class);
    }

    @DisplayName("Should fail when method does not exists")
    @Test
    void should_fail_when_method_does_not_exists() {
        assertThrows(Throwable.class, () ->
                ClassAssertion.of(NoFieldSupportClass.class).method("betAttrib"));
        assertThrows(Throwable.class, () ->
                ClassAssertion.of(NoFieldSupportClass.class).method("setAttrib"));
    }

}
