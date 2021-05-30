package com.acpreda.test.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Utility class that provides assertions
 *
 * @author Arturo Cruz
 * @since 1.0.0
 */
public class ReflectionAssertions {

    private static final String TEST_STRING = "TEST_STRING";
    private static final Integer TEST_INTEGER = 1443;
    private static final int TEST_INT = 1443;
    private static final Long TEST_LONG = 5587L;
    private static final long TEST_LONGP = 5589L;
    private static final LocalDateTime TEST_LOCAL_DATE_TIME = LocalDateTime.now();

    private static final Map<Class<?>, Object> TEST_DATA;

    static {
        TEST_DATA = new HashMap<>();
        TEST_DATA.put(String.class, TEST_STRING);
        TEST_DATA.put(Integer.class, TEST_INTEGER);
        TEST_DATA.put(int.class, TEST_INT);
        TEST_DATA.put(Long.class, TEST_LONG);
        TEST_DATA.put(long.class, TEST_LONGP);
        TEST_DATA.put(LocalDateTime.class, TEST_LOCAL_DATE_TIME);
    }

    /**
     * Asserts that the subject class is annotated with the annotation class
     *
     * @param subject    The class to be checked
     * @param annotation The annotation class that has to be present in the subject class
     */
    public static void assertIsAnnotated(Class<?> subject, Class<? extends Annotation> annotation) {
        Annotation x = subject.getAnnotation(annotation);
        if (x == null) {
            fail("Class " + subject.getName() + " is not annotated with " + annotation.getName() +
                    ". Maybe the retention policy is not RUNTIME");
        }
    }

    /**
     * Inspects the subject class to assert that it is actually a class that follows the POJO pattern
     *
     * @param subject The class to be inspected
     */
    public static void assertIsPojo(Class<?> subject) {
        assertHasDefaultConstructor(subject);
        Object obj = null;
        try {
            Constructor<?> constructor = subject.getConstructor();
            obj = constructor.newInstance();
        } catch (Exception e) {
            fail("Could not create object with default constructor: " + subject.getName());
        }
        Field[] fields = subject.getDeclaredFields();
        for (Field field : fields) {
            if (isNotProperty(field))
                continue;
            String getterName = getter(field);
            String setterName = setter(field);
            Method getter = null;
            Method setter = null;
            try {
                getter = subject.getDeclaredMethod(getterName);
                assertNotNull(getter);
                setter = subject.getDeclaredMethod(setterName, field.getType());
                assertNotNull(setter);
            } catch (NoSuchMethodException e) {
                fail("Getter or Setter not found for: " + field.getName());
            }
            Class<?> fieldType = field.getType();
            Object testData = TEST_DATA.get(fieldType);
            if (testData == null) {
                fail("No test data found for type: " + fieldType.getName());
            }
            try {
                setter.invoke(obj, testData);
            } catch (Exception e) {
                fail("Could not invoke setter: " + setterName);
            }
            Object returnedByGetter = null;
            try {
                returnedByGetter = getter.invoke(obj);
            } catch (Exception e) {
                fail("Could not invoke getter: " + getterName);
            }
            if (returnedByGetter == null) {
                fail("Returned value is null: " + getterName);
            }
            if (!returnedByGetter.equals(testData)) {
                fail("Returned value is not the testData: " + getterName);
            }
        }
    }

    /**
     * Inspects the subject class looking for a constructor with no arguments
     *
     * @param subject The class to be inspected
     */
    public static void assertHasDefaultConstructor(Class<?> subject) {
        try {
            subject.getConstructor();
        } catch (NoSuchMethodException e) {
            fail("Does not have default constructor: " + subject.getName());
        }
    }

    private static boolean isNotProperty(Field field) {
        int mods = field.getModifiers();
        return (mods & Modifier.PRIVATE) != Modifier.PRIVATE
                || (mods & Modifier.STATIC) == Modifier.STATIC
                || (mods & Modifier.TRANSIENT) == Modifier.TRANSIENT;
    }

    private static String setter(Field field) {
        String fieldName = field.getName();
        return "set" + fieldName.substring(0, 1).toUpperCase(Locale.ROOT) + fieldName.substring(1);
    }

    private static String getter(Field field) {
        String fieldName = field.getName();
        return "get" + fieldName.substring(0, 1).toUpperCase(Locale.ROOT) + fieldName.substring(1);
    }

    /**
     * Inspects a subject class looking for a specific method
     *
     * @param subject        The class to be inspected
     * @param methodName     The method name
     * @param parameterTypes The parameters of the method
     */
    public static void assertHasMethod(Class<?> subject, String methodName, Class<?>... parameterTypes) {
        try {
            subject.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            fail("Method not found in " + subject.getName() + ": " + methodName);
        }
    }
}
