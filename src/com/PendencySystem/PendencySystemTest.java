package com.PendencySystem;

import com.PendencySystem.exceptions.DuplicateEntityException;
import com.PendencySystem.exceptions.NotFoundException;
import com.PendencySystem.repository.impl.TagRepositoryImpl;
import com.PendencySystem.repository.impl.TransactionRepositoryImpl;
import com.PendencySystem.service.impl.PendencyManagementServiceImpl;
import com.PendencySystem.service.interfaces.PendencyManagementService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PendencySystemTest {

    private static int passed = 0, failed = 0;

    public static void main(String[] args) {
        testBasicTrackingAndCounts();
        testStopTrackingDecrementsCounts();
        testInvalidHierarchyReturnsZero();
        testDuplicateEntityThrows();
        testStopNonExistentThrows();
        testNullAndEmptyValidation();
        testGenericHierarchy();
        testConcurrentTracking();

        System.out.println("\n=== Test Results: " + passed + " passed, " + failed + " failed ===");
    }

    static PendencyManagementService freshService() {
        return new PendencyManagementServiceImpl(
                new TransactionRepositoryImpl(), new TagRepositoryImpl());
    }

    static void testBasicTrackingAndCounts() {
        PendencyManagementService svc = freshService();
        svc.startTracking(1, Arrays.asList("UPI", "Karnataka", "Bangalore"));
        svc.startTracking(2, Arrays.asList("UPI", "Karnataka", "Mysore"));
        svc.startTracking(3, Arrays.asList("UPI", "Rajasthan", "Jaipur"));
        svc.startTracking(4, Arrays.asList("Wallet", "Karnataka", "Bangalore"));

        assertEqual("UPI count", 3, svc.getCounts(Arrays.asList("UPI")));
        assertEqual("UPI+Karnataka", 2, svc.getCounts(Arrays.asList("UPI", "Karnataka")));
        assertEqual("UPI+KA+BLR", 1, svc.getCounts(Arrays.asList("UPI", "Karnataka", "Bangalore")));
        assertEqual("Wallet count", 1, svc.getCounts(Arrays.asList("Wallet")));
    }

    static void testStopTrackingDecrementsCounts() {
        PendencyManagementService svc = freshService();
        svc.startTracking(1, Arrays.asList("UPI", "Karnataka", "Bangalore"));
        svc.startTracking(2, Arrays.asList("UPI", "Karnataka", "Mysore"));
        svc.startTracking(3, Arrays.asList("UPI", "Rajasthan", "Jaipur"));
        svc.startTracking(4, Arrays.asList("Wallet", "Karnataka", "Bangalore"));
        svc.startTracking(5, Arrays.asList("Wallet", "Karnataka", "Bangalore"));

        svc.stopTracking(1);
        svc.stopTracking(2);

        assertEqual("UPI after stops", 1, svc.getCounts(Arrays.asList("UPI")));
        assertEqual("UPI+KA+BLR after stops", 0, svc.getCounts(Arrays.asList("UPI", "Karnataka", "Bangalore")));
        assertEqual("Wallet unchanged", 2, svc.getCounts(Arrays.asList("Wallet")));
    }

    static void testInvalidHierarchyReturnsZero() {
        PendencyManagementService svc = freshService();
        svc.startTracking(1, Arrays.asList("UPI", "Karnataka", "Bangalore"));

        assertEqual("non-root tag", 0, svc.getCounts(Arrays.asList("Bangalore")));
        assertEqual("unknown tag", 0, svc.getCounts(Arrays.asList("NetBanking")));
        assertEqual("partial wrong", 0, svc.getCounts(Arrays.asList("UPI", "Delhi")));
    }

    static void testDuplicateEntityThrows() {
        PendencyManagementService svc = freshService();
        svc.startTracking(1, Arrays.asList("UPI", "Karnataka"));
        assertThrows("duplicate entity", DuplicateEntityException.class,
                () -> svc.startTracking(1, Arrays.asList("Wallet", "Delhi")));
    }

    static void testStopNonExistentThrows() {
        PendencyManagementService svc = freshService();
        assertThrows("stop non-existent", NotFoundException.class,
                () -> svc.stopTracking(999));
    }

    static void testNullAndEmptyValidation() {
        PendencyManagementService svc = freshService();
        assertThrows("null id", IllegalArgumentException.class,
                () -> svc.startTracking(null, Arrays.asList("A")));
        assertThrows("null tags", IllegalArgumentException.class,
                () -> svc.startTracking(1, null));
        assertThrows("empty tags", IllegalArgumentException.class,
                () -> svc.startTracking(1, Collections.emptyList()));
        assertThrows("getCounts null", IllegalArgumentException.class,
                () -> svc.getCounts(null));
    }

    static void testGenericHierarchy() {
        PendencyManagementService svc = freshService();
        svc.startTracking(1, Arrays.asList("Mumbai", "PizzaHut", "Italian", "Margherita"));
        svc.startTracking(2, Arrays.asList("Mumbai", "PizzaHut", "Italian", "Pepperoni"));
        svc.startTracking(3, Arrays.asList("Mumbai", "Dominos", "Italian", "Farmhouse"));
        svc.startTracking(4, Arrays.asList("Delhi", "PizzaHut", "Italian", "Margherita"));

        assertEqual("4 tags deep: Mumbai", 3, svc.getCounts(Arrays.asList("Mumbai")));
        assertEqual("4 tags deep: Mumbai+PizzaHut", 2, svc.getCounts(Arrays.asList("Mumbai", "PizzaHut")));
        assertEqual("4 tags deep: Delhi", 1, svc.getCounts(Arrays.asList("Delhi")));

        svc.stopTracking(1);
        assertEqual("after stop: Mumbai+PizzaHut", 1, svc.getCounts(Arrays.asList("Mumbai", "PizzaHut")));
    }

    static void testConcurrentTracking() {
        PendencyManagementService svc = freshService();
        int numThreads = 10, perThread = 100;
        List<Thread> threads = new ArrayList<>();

        for (int t = 0; t < numThreads; t++) {
            final int base = t * perThread;
            threads.add(new Thread(() -> {
                for (int i = 0; i < perThread; i++) {
                    svc.startTracking(base + i, Arrays.asList("Root", "Child"));
                }
            }));
        }
        threads.forEach(Thread::start);
        threads.forEach(t -> { try { t.join(); } catch (InterruptedException e) {} });

        assertEqual("concurrent: 1000 tracked", 1000, svc.getCounts(Arrays.asList("Root")));
        assertEqual("concurrent: leaf", 1000, svc.getCounts(Arrays.asList("Root", "Child")));

        // stop all concurrently
        List<Thread> stopThreads = new ArrayList<>();
        for (int t = 0; t < numThreads; t++) {
            final int base = t * perThread;
            stopThreads.add(new Thread(() -> {
                for (int i = 0; i < perThread; i++) {
                    svc.stopTracking(base + i);
                }
            }));
        }
        stopThreads.forEach(Thread::start);
        stopThreads.forEach(t -> { try { t.join(); } catch (InterruptedException e) {} });

        assertEqual("concurrent: all stopped", 0, svc.getCounts(Arrays.asList("Root")));
    }

    // --- Assertion Helpers ---

    static void assertEqual(String name, int expected, int actual) {
        if (expected == actual) { passed++; System.out.println("  PASS: " + name); }
        else { failed++; System.out.println("  FAIL: " + name + " (expected=" + expected + ", actual=" + actual + ")"); }
    }

    static void assertThrows(String name, Class<? extends Exception> type, Runnable r) {
        try { r.run(); failed++; System.out.println("  FAIL (no exception): " + name); }
        catch (Exception e) {
            if (type.isInstance(e)) { passed++; System.out.println("  PASS: " + name + " -> " + e.getMessage()); }
            else { failed++; System.out.println("  FAIL (wrong exception): " + name + " -> " + e.getClass().getSimpleName()); }
        }
    }
}
