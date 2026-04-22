package com.BattleShipGame;

import com.BattleShipGame.service.interfaces.GameService;
import com.BattleShipGame.service.impl.GameServiceImpl;
import com.BattleShipGame.strategy.interfaces.Strategy;
import com.BattleShipGame.strategy.impl.RandomStrategy;

public class BattleShipGameTest {
    private static int passed = 0, failed = 0;

    private static void assertEquals(Object expected, Object actual, String msg) {
        if (expected.equals(actual)) {
            System.out.println("PASS: " + msg);
            passed++;
        } else {
            System.out.println("FAIL: " + msg + " (expected=" + expected + ", actual=" + actual + ")");
            failed++;
        }
    }

    private static void assertThrows(Runnable r, Class<? extends Exception> type, String msg) {
        try {
            r.run();
            System.out.println("FAIL: " + msg + " (no exception thrown)");
            failed++;
        } catch (Exception e) {
            if (type.isInstance(e)) {
                System.out.println("PASS: " + msg + " (" + e.getMessage() + ")");
                passed++;
            } else {
                System.out.println("FAIL: " + msg + " (wrong exception: " + e.getClass().getSimpleName() + ")");
                failed++;
            }
        }
    }

    public static void main(String[] args) {
        testInitGameOddSize();
        testShipPlacementOutOfTerritory();
        testShipOverlap();
        testUnequalFleetPreventsStart();
        testNoShipsPreventsStart();
        testInvalidPlayer();
        testFullGameRuns();
        testNegativeShipSize();

        System.out.println("\n--- Results: " + passed + " passed, " + failed + " failed ---");
    }

    static void testInitGameOddSize() {
        Strategy s = new RandomStrategy();
        GameService gs = new GameServiceImpl(s, s);
        assertThrows(() -> gs.initGame(7), IllegalArgumentException.class,
                "initGame rejects odd size");
    }

    static void testShipPlacementOutOfTerritory() {
        Strategy s = new RandomStrategy();
        GameService gs = new GameServiceImpl(s, s);
        gs.initGame(8);
        assertEquals(false, gs.addShip("A", 3, 0, 2),
                "PlayerA ship overflows into B territory");
        assertEquals(false, gs.addShip("B", 3, 0, 2),
                "PlayerB ship placed in A territory");
    }

    static void testShipOverlap() {
        Strategy s = new RandomStrategy();
        GameService gs = new GameServiceImpl(s, s);
        gs.initGame(8);
        assertEquals(true, gs.addShip("A", 0, 0, 2), "First ship placed");
        assertEquals(false, gs.addShip("A", 1, 1, 2), "Overlapping ship rejected");
    }

    static void testUnequalFleetPreventsStart() {
        Strategy s = new RandomStrategy();
        GameService gs = new GameServiceImpl(s, s);
        gs.initGame(8);
        gs.addShip("A", 0, 0, 2);
        gs.addShip("A", 2, 0, 2);
        gs.addShip("B", 4, 0, 2);
        assertThrows(() -> gs.startGame(), IllegalStateException.class,
                "Unequal fleet count prevents game start");
    }

    static void testNoShipsPreventsStart() {
        Strategy s = new RandomStrategy();
        GameService gs = new GameServiceImpl(s, s);
        gs.initGame(8);
        assertThrows(() -> gs.startGame(), IllegalStateException.class,
                "No ships prevents game start");
    }

    static void testInvalidPlayer() {
        Strategy s = new RandomStrategy();
        GameService gs = new GameServiceImpl(s, s);
        gs.initGame(8);
        assertThrows(() -> gs.addShip("C", 0, 0, 2), IllegalArgumentException.class,
                "Invalid player name rejected");
    }

    static void testFullGameRuns() {
        Strategy s = new RandomStrategy();
        GameService gs = new GameServiceImpl(s, s);
        gs.initGame(8);
        gs.addShip("A", 0, 0, 2);
        gs.addShip("B", 4, 0, 2);
        try {
            gs.startGame();
            System.out.println("PASS: Full game completes without error");
            passed++;
        } catch (Exception e) {
            System.out.println("FAIL: Full game threw " + e);
            failed++;
        }
    }

    static void testNegativeShipSize() {
        Strategy s = new RandomStrategy();
        GameService gs = new GameServiceImpl(s, s);
        gs.initGame(8);
        assertThrows(() -> gs.addShip("A", 0, 0, -1), IllegalArgumentException.class,
                "Negative ship size rejected");
    }
}
