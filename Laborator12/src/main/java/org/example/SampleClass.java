package org.example;

public class SampleClass {
    @Test
    public static void testMethod1() {
        System.out.println("testMethod1 executed");
    }

    @Test
    public static void testMethod2() {
        System.out.println("testMethod2 executed");
    }

    @Test
    public void nonStaticMethod() {
        System.out.println("This is a non-static method");
    }

    public void nonStaticMethod2() {
        System.out.println("This is a non-static method");
    }
}