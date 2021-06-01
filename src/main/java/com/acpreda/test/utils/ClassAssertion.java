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
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class ClassAssertion {

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

    private final Class<?> subject;

    private ClassAssertion(Class<?> subject) {
        this.subject = subject;
    }

    public static ClassAssertion of(Class<?> subject) {
        return new ClassAssertion(subject);
    }

    public Class<?> getSubject() {
        return this.subject;
    }

    public AnnotationAssertion isAnnotated(Class<? extends Annotation> annotation) {
        Annotation extracted = subject.getAnnotation(annotation);
        assertNotNull(extracted);
        return AnnotationAssertion.of(extracted);
    }

    public ClassAssertion hasDefaultConstructor() {
        try {
            subject.getConstructor();
        } catch (Exception ignored) {
            fail("Does not have default constructor: " + subject.getName());
        }
        return this;
    }

    public ClassAssertion isPojo() {
        hasDefaultConstructor();
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
        return this;
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

    public MethodAssertion method(String methodName, Class<?>... parameterTypes) {
        try {
            Method method = subject.getDeclaredMethod(methodName, parameterTypes);
            return MethodAssertion.of(method);
        } catch (NoSuchMethodException e) {
            fail("Method not found in " + subject.getName() + ": " + methodName);
        }
        return null;
    }

    public ClassAssertion assignableFrom(Class<?> assignable) {
        if (!assignable.isAssignableFrom(subject)) {
            fail("Expected to be assignable from " + assignable.getName());
        }
        return this;
    }

    public FieldAssertion field(String name) {
        Field field = null;
        try {
            field = subject.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            fail("Field " + name + " not found");
        }
        return FieldAssertion.of(field);
    }

    public ClassAssertion eachProperty(Consumer<FieldAssertion> consumer) {
        Field[] fields = subject.getDeclaredFields();
        for (Field field : fields) {
            if (isNotProperty(field)) {
                continue;
            }
            consumer.accept(FieldAssertion.of(field));
        }
        return this;
    }

}
