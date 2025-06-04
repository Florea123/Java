package org.example;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;


public class ClassAnalyzer {
    private static int executedTests = 0;
    private static int skippedTests = 0;
    private static int failedTests = 0;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java ClassAnalyzer <path-to-class-or-folder-or-jar>");
            return;
        }
        File input = new File(args[0]);
        if (!input.exists()) {
            System.out.println("Invalid path: " + args[0]);
            return;
        }

        try {
            if (input.isFile()) {
                if (input.getName().endsWith(".class")) {
                    analyzeClass(input.getAbsolutePath());
                } else if (input.getName().endsWith(".jar")) {
                    analyzeJar(input);
                }
            } else if (input.isDirectory()) {
                analyzeFolder(input);
            }

            printStatistics();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void analyzeFolder(File folder) throws Exception {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                analyzeFolder(file);
            } else if (file.getName().endsWith(".class")) {
                analyzeClass(file.getAbsolutePath());
            } else if (file.getName().endsWith(".jar")) {
                analyzeJar(file);
            }
        }
    }

    private static void analyzeJar(File jarFile) throws Exception {
        try (JarFile jar = new JarFile(jarFile)) {
            URL[] urls = {new URL("jar:file:" + jarFile.getAbsolutePath() + "!/")};
            URLClassLoader loader = URLClassLoader.newInstance(urls);

            jar.stream()
                    .filter(entry -> entry.getName().endsWith(".class"))
                    .forEach(entry -> {
                        try {
                            String className = entry.getName().replace("/", ".").replace(".class", "");
                            Class<?> clazz = loader.loadClass(className);
                            analyzeClass(clazz);
                        } catch (Exception e) {
                            System.out.println("Failed to load class: " + entry.getName());
                        }
                    });
        }
    }

    private static void analyzeClass(String classPath) throws Exception {
        String basePath = classPath;
        int orgIndex = basePath.indexOf(File.separator + "org" + File.separator);
            basePath = basePath.substring(0, orgIndex);
            String relativePath = classPath.substring(orgIndex + 1);
            String className = relativePath.replace(File.separator, ".").replace(".class", "");
            URLClassLoader loader = URLClassLoader.newInstance(new URL[]{new File(basePath).toURI().toURL()});
            Class<?> clazz = loader.loadClass(className);
            analyzeClass(clazz);
    }

    private static void analyzeClass(Class<?> clazz) {
        if (!Modifier.isPublic(clazz.getModifiers())) {
            return;
        }

        System.out.println("Analyzing class: " + clazz.getName());
        boolean hasTestMethods = false;

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                hasTestMethods = true;
                invokeTestMethod(clazz, method);
            }
        }

        if (!hasTestMethods) {
            skippedTests++;
        }
    }

    private static void invokeTestMethod(Class<?> clazz, Method method) {
        try {
            Object instance = Modifier.isStatic(method.getModifiers()) ? null : clazz.getDeclaredConstructor().newInstance();
            Object[] mockArgs = generateMockArguments(method.getParameterTypes());

            System.out.println("Invoking: " + method.getName());
            method.setAccessible(true);
            method.invoke(instance, mockArgs);
            executedTests++;
        } catch (Exception e) {
            System.out.println("Failed to invoke: " + method.getName());
            failedTests++;
        }
    }

    private static Object[] generateMockArguments(Class<?>[] parameterTypes) {
        List<Object> mockArgs = new ArrayList<>();
        for (Class<?> paramType : parameterTypes) {
            if (paramType == int.class) {
                mockArgs.add(0);
            } else if (paramType == boolean.class) {
                mockArgs.add(false);
            } else if (paramType == double.class) {
                mockArgs.add(0.0);
            } else if (paramType == String.class) {
                mockArgs.add("mock");
            } else {
                mockArgs.add(null);
            }
        }
        return mockArgs.toArray();
    }

    private static void printStatistics() {
        System.out.println("\nTest Statistics:");
        System.out.println("Executed: " + executedTests);
        System.out.println("Skipped: " + skippedTests);
        System.out.println("Failed: " + failedTests);
    }
}