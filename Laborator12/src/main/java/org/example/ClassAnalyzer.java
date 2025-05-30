package org.example;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

// Define the @Test annotation
@Retention(RetentionPolicy.RUNTIME)
@interface Test {
}

public class ClassAnalyzer {
    public static void main() {
        try {
            String className = "org.example.SampleClass";

            Class<?> clazz = Class.forName(className);

            System.out.println("Methods in class " + className + ":");
            for (Method method : clazz.getDeclaredMethods()) {
                System.out.println("- " + method);
            }

            System.out.println("\nInvoking @Test methods:");
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Test.class)) {
                    if (method.getParameterCount() == 0 && java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                        System.out.println("Invoking: " + method.getName());
                        method.invoke(null);
                    } else {
                        System.out.println("Skipping: " + method.getName() + " (not static or has parameters)");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}