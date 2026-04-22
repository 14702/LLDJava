package com.MultiLevelCache;

import com.MultiLevelCache.factory.CacheLevelFactory;
import com.MultiLevelCache.model.interfaces.CacheLevel;
import com.MultiLevelCache.model.ReadResult;
import com.MultiLevelCache.model.WriteResult;
import com.MultiLevelCache.service.impl.MultiLevelCacheService;
import com.MultiLevelCache.service.interfaces.CacheService;
import com.MultiLevelCache.stats.impl.SlidingWindowStatsTracker;
import com.MultiLevelCache.stats.interfaces.StatsTracker;

import java.util.Arrays;

public class MultiLevelCacheApplication {

    private static final String LINE = "-------------------------------------------------------------------";
    private static int passed = 0, failed = 0;

    public static void main(String[] args) {

        // Create levels via Factory (default LRU eviction)
        CacheLevel l1 = CacheLevelFactory.create(1, 3, 1, 2);
        CacheLevel l2 = CacheLevelFactory.create(2, 5, 3, 4);
        CacheLevel l3 = CacheLevelFactory.create(3, 7, 5, 6);

        StatsTracker statsTracker = new SlidingWindowStatsTracker(10);
        CacheService cache = new MultiLevelCacheService(Arrays.asList(l1, l2, l3), statsTracker);

        // === WRITE TESTS ===
        section("WRITE OPERATIONS");

        WriteResult wr = cache.write("A", "1");
        System.out.println("WRITE A=1 | Time: " + wr.getTotalTime());
        assertEqual("Write A=1 time", 21, wr.getTotalTime());

        wr = cache.write("B", "2");
        System.out.println("WRITE B=2 | Time: " + wr.getTotalTime());
        assertEqual("Write B=2 time", 21, wr.getTotalTime());

        wr = cache.write("C", "3");
        System.out.println("WRITE C=3 | Time: " + wr.getTotalTime());
        assertEqual("Write C=3 time", 21, wr.getTotalTime());

        cache.stat();

        // === READ TESTS ===
        section("READ OPERATIONS");

        ReadResult rr = cache.read("A");
        System.out.println("READ A | Value: " + rr.getValue() + " | Time: " + rr.getTotalTime());
        assertEqual("Read A value", "1", rr.getValue());
        assertEqual("Read A time", 1, rr.getTotalTime());

        rr = cache.read("Z");
        System.out.println("READ Z | Value: " + rr.getValue() + " | Time: " + rr.getTotalTime());
        assertEqual("Read Z value", null, rr.getValue());
        assertEqual("Read Z time", 9, rr.getTotalTime());

        // === WRITE WITH EARLY STOP ===
        section("WRITE WITH EARLY STOP");

        wr = cache.write("A", "1");
        System.out.println("WRITE A=1 (same) | Time: " + wr.getTotalTime());
        assertEqual("Write A=1 early stop time", 1, wr.getTotalTime());

        // === LRU EVICTION TEST ===
        section("LRU EVICTION (L1 capacity=3)");

        wr = cache.write("D", "4");
        System.out.println("WRITE D=4 | Time: " + wr.getTotalTime());

        rr = cache.read("B");
        System.out.println("READ B (evicted from L1) | Value: " + rr.getValue() + " | Time: " + rr.getTotalTime());
        assertEqual("Read B value after eviction", "2", rr.getValue());
        assertEqual("Read B time (miss L1, hit L2, write-back L1)", 6, rr.getTotalTime());

        // === READ WRITE-BACK TEST ===
        section("READ WRITE-BACK");

        cache.write("E", "5");
        cache.write("F", "6");
        cache.write("G", "7");
        cache.write("H", "8");

        rr = cache.read("E");
        System.out.println("READ E | Value: " + rr.getValue() + " | Time: " + rr.getTotalTime());
        assertEqual("Read E value", "5", rr.getValue());

        // === WRITE WITH VALUE CHANGE ===
        section("WRITE WITH VALUE CHANGE");

        wr = cache.write("A", "100");
        System.out.println("WRITE A=100 | Time: " + wr.getTotalTime());

        rr = cache.read("A");
        System.out.println("READ A | Value: " + rr.getValue());
        assertEqual("Read A after update", "100", rr.getValue());

        // === DYNAMIC LEVEL — ADD L4 at runtime ===
        section("DYNAMIC: ADD L4 AT RUNTIME");

        cache.stat();
        CacheLevel l4 = CacheLevelFactory.create(4, 10, 7, 8);
        cache.addLevel(l4);
        System.out.println("Added L4 (cap=10, read=7, write=8)");

        wr = cache.write("X", "99");
        System.out.println("WRITE X=99 across 4 levels | Time: " + wr.getTotalTime());
        // Time = (1+2)+(3+4)+(5+6)+(7+8) = 36
        assertEqual("Write X=99 across 4 levels", 36, wr.getTotalTime());

        cache.stat();

        // === DYNAMIC LEVEL — REMOVE L4 at runtime ===
        section("DYNAMIC: REMOVE L4 AT RUNTIME");

        cache.removeLevel(4);
        System.out.println("Removed L4");

        wr = cache.write("Y", "77");
        System.out.println("WRITE Y=77 across 3 levels | Time: " + wr.getTotalTime());
        assertEqual("Write Y=77 back to 3 levels", 21, wr.getTotalTime());

        cache.stat();

        // === SUMMARY ===
        section("TEST RESULTS");
        System.out.println("Passed: " + passed + " | Failed: " + failed);
        if (failed == 0) {
            System.out.println("ALL TESTS PASSED");
        }
    }

    private static void section(String title) {
        System.out.println(LINE);
        System.out.println(title);
        System.out.println(LINE);
    }

    private static void assertEqual(String label, Object expected, Object actual) {
        if ((expected == null && actual == null) || (expected != null && expected.equals(actual))) {
            passed++;
        } else {
            failed++;
            System.out.println("  FAIL: " + label + " — expected " + expected + " but got " + actual);
        }
    }

    private static void assertEqual(String label, int expected, int actual) {
        assertEqual(label, (Object) expected, (Object) actual);
    }
}
