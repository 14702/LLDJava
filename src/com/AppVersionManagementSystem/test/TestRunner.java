package com.AppVersionManagementSystem.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    private static int passed = 0;
    private static int failed = 0;
    private static final List<String> failures = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  App Version Management System Tests");
        System.out.println("========================================\n");

        runTestClass(VersionTest.class);
        runTestClass(AppVersionDetailsRepositoryTest.class);
        runTestClass(DeviceRepositoryTest.class);
        runTestClass(FileServiceTest.class);
        runTestClass(AppServiceTest.class);
        runTestClass(RolloutStrategyTest.class);
        runTestClass(ConcurrencyTest.class);

        System.out.println("\n========================================");
        System.out.println("  Results: " + passed + " passed, " + failed + " failed");
        System.out.println("========================================");

        if (!failures.isEmpty()) {
            System.out.println("\nFailures:");
            failures.forEach(f -> System.out.println("  - " + f));
        }

        System.exit(failed > 0 ? 1 : 0);
    }

    private static void runTestClass(Class<?> testClass) {
        System.out.println("[" + testClass.getSimpleName() + "]");
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.getName().startsWith("test")) {
                try {
                    Object instance = testClass.getDeclaredConstructor().newInstance();
                    method.invoke(instance);
                    passed++;
                    System.out.println("  PASS: " + method.getName());
                } catch (Exception e) {
                    failed++;
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    String msg = testClass.getSimpleName() + "." + method.getName()
                            + " -> " + cause.getMessage();
                    failures.add(msg);
                    System.out.println("  FAIL: " + method.getName() + " -> " + cause.getMessage());
                }
            }
        }
        System.out.println();
    }

    public static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) return;
        if (expected != null && expected.equals(actual)) return;
        throw new AssertionError(message + " | Expected: " + expected + ", Actual: " + actual);
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertThrows(Class<? extends Throwable> expected, Runnable action,
                                     String message) {
        try {
            action.run();
            throw new AssertionError(message + " | Expected exception "
                    + expected.getSimpleName() + " but none was thrown");
        } catch (Throwable t) {
            if (!expected.isInstance(t)) {
                throw new AssertionError(message + " | Expected " + expected.getSimpleName()
                        + " but got " + t.getClass().getSimpleName());
            }
        }
    }

    public static void assertNotNull(Object obj, String message) {
        if (obj == null) {
            throw new AssertionError(message);
        }
    }

    public static void assertNull(Object obj, String message) {
        if (obj != null) {
            throw new AssertionError(message + " | Expected null but got: " + obj);
        }
    }

    static class AssertionError extends RuntimeException {
        AssertionError(String message) {
            super(message);
        }
    }
}
