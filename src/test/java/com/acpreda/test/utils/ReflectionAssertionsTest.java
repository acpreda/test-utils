package com.acpreda.test.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReflectionAssertionsTest { 

    @Test
    @DisplayName("Verificación de anotación por herencia")
    void debe_detectar_anotaciones_por_herencia() {
        ReflectionAssertions.assertIsAnnotated(AnnotatedClass.class, TestingAnnotation.class);
        Assertions.assertThrows(
                Throwable.class,
                () -> ReflectionAssertions.assertIsAnnotated(
                        NotAnnotatedClass.class,
                        TestingAnnotation.class
                )
        );
    }

}