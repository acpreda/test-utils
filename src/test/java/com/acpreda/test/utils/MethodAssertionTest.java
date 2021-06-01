package com.acpreda.test.utils;

import com.acpreda.test.utils.samples.NoSetClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MethodAssertionTest {

    @DisplayName("Should pass when method is annotated")
    @Test
    void should_pass_when_method_is_annotated() throws NoSuchMethodException {
        Method method = NoSetClass.class.getDeclaredMethod("getAttrib");
        MethodAssertion methodAssertion = MethodAssertion.of(method);
        assertNotNull(methodAssertion);
    }

}