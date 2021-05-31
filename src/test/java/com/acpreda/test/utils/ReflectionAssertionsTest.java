package com.acpreda.test.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ReflectionAssertionsTest {

    @Test
    @DisplayName("Verificación de anotación por herencia")
    void debe_detectar_anotaciones_por_herencia() {
        ReflectionAssertions.assertIsAnnotated(AnnotatedClass.class, TestingAnnotation.class);
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

}