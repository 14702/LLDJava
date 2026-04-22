package com.SnakeAndLadder;

import com.SnakeAndLadder.enums.ElementType;
import com.SnakeAndLadder.model.*;
import com.SnakeAndLadder.service.impl.GameServiceImpl;
import com.SnakeAndLadder.service.interfaces.GameService;
import com.SnakeAndLadder.strategy.impl.SumStrategy;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SnakeAndLadderTest {

    private static int passed = 0, failed = 0;

    public static void main(String[] args) {
        testSnakeMoveDown();
        testLadderMoveUp();
        testCrocodileMoveBack5();
        testCrocodileNearStart();
        testDiceRange();
        testPlayerWinsExactly();
        testPlayerCantMoveBeyondBoard();
        testSnakeAndLadderChain();
        testMineSkipsTurns();
        testManualDiceOverride();
        testPlayerStartPosition();

        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }

    static GameService freshGame(int boardSize) {
        GameService gs = new GameServiceImpl();
        gs.addBoardSize(boardSize);
        gs.numOfDice(1);
        gs.addStrategy(new SumStrategy(new Dice()));
        return gs;
    }

    static void testSnakeMoveDown() {
        SnakeMove snake = new SnakeMove(ElementType.SNAKE);
        assertTrue("snake rejects end >= start", !snake.setMove(10, 20));
        assertTrue("snake accepts end < start", snake.setMove(50, 10));
        assertEqual("snake destination", 10, snake.getPos());
    }

    static void testLadderMoveUp() {
        LadderMove ladder = new LadderMove(ElementType.LADDER);
        assertTrue("ladder rejects end <= start", !ladder.setMove(50, 10));
        assertTrue("ladder accepts end > start", ladder.setMove(10, 50));
        assertEqual("ladder destination", 50, ladder.getPos());
    }

    static void testCrocodileMoveBack5() {
        CrocodileMove croc = new CrocodileMove(ElementType.CROCODILE);
        croc.setMove(30, 0);
        assertEqual("crocodile moves back 5", 25, croc.getPos());
    }

    static void testCrocodileNearStart() {
        CrocodileMove croc = new CrocodileMove(ElementType.CROCODILE);
        croc.setMove(3, 0);
        assertEqual("crocodile near start clamps to 0", 0, croc.getPos());
    }

    static void testDiceRange() {
        Dice dice = new Dice();
        for (int i = 0; i < 100; i++) {
            int val = dice.rollDice();
            assertTrue("dice in range [1,6]: got " + val, val >= 1 && val <= 6);
        }
    }

    static void testPlayerWinsExactly() {
        GameService gs = freshGame(10);
        gs.addPlayer("Alice", 0);
        gs.addPlayer("Bob", 0);
        // Alice rolls 5 -> pos 5, Bob rolls 1 -> pos 1
        // Alice rolls 5 -> pos 10 -> WIN
        gs.setManualDice(new int[]{5, 1, 5});
        String output = captureOutput(() -> gs.beginGame());
        assertTrue("Alice wins", output.contains("Alice wins"));
    }

    static void testPlayerCantMoveBeyondBoard() {
        GameService gs = freshGame(10);
        gs.addPlayer("Alice", 8);
        gs.addPlayer("Bob", 0);
        // Alice at 8, rolls 5 -> 13 > 10, stays at 8
        // Bob rolls 2 -> pos 2
        // Alice rolls 2 -> pos 10 -> WIN
        gs.setManualDice(new int[]{5, 2, 2});
        String output = captureOutput(() -> gs.beginGame());
        assertTrue("Alice stays when overshooting", output.contains("moved from 8 to 8"));
        assertTrue("Alice wins eventually", output.contains("Alice wins"));
    }

    static void testSnakeAndLadderChain() {
        GameService gs = freshGame(20);
        // Snake at 5 -> 3, Ladder at 3 -> 15
        gs.addElements(5, 3, ElementType.SNAKE);
        gs.addElements(3, 15, ElementType.LADDER);
        gs.addPlayer("Alice", 0);
        gs.addPlayer("Bob", 0);
        // Alice rolls 5 -> hits snake at 5 -> goes to 3 -> chains to ladder -> goes to 15
        gs.setManualDice(new int[]{5, 1, 5});
        String output = captureOutput(() -> gs.beginGame());
        assertTrue("chain detected", output.contains("chained LADDER at 3 to 15"));
        assertTrue("Alice wins at 20", output.contains("Alice wins"));
    }

    static void testMineSkipsTurns() {
        GameService gs = freshGame(20);
        gs.addElements(5, 0, ElementType.MINE);
        gs.addPlayer("Alice", 0);
        gs.addPlayer("Bob", 0);
        // Alice rolls 5 -> mine at 5, skip 2 turns
        // Bob rolls 3 -> pos 3
        // Alice skip turn 1
        // Bob rolls 3 -> pos 6
        // Alice skip turn 2
        // Bob rolls 4 -> pos 10
        // Alice rolls 5 -> pos 10 (mine position, now moves from 5 to 10)
        gs.setManualDice(new int[]{5, 3, 3, 4, 5, 6, 4});
        String output = captureOutput(() -> gs.beginGame());
        assertTrue("mine hit", output.contains("hit a mine at 5"));
        assertTrue("skip turn logged", output.contains("stuck in a mine, skipping turn"));
    }

    static void testManualDiceOverride() {
        GameService gs = freshGame(20);
        gs.addPlayer("Alice", 0);
        gs.addPlayer("Bob", 0);
        gs.setManualDice(new int[]{3, 4, 6});
        String output = captureOutput(() -> gs.beginGame());
        // Alice rolls 3 -> pos 3
        assertTrue("Alice rolled 3", output.contains("Alice rolled a 3 and moved from 0 to 3"));
        // Bob rolls 4 -> pos 4
        assertTrue("Bob rolled 4", output.contains("Bob rolled a 4 and moved from 0 to 4"));
    }

    static void testPlayerStartPosition() {
        GameService gs = freshGame(20);
        gs.addPlayer("Alice", 5);
        gs.addPlayer("Bob", 10);
        gs.setManualDice(new int[]{3, 2});
        String output = captureOutput(() -> gs.beginGame());
        assertTrue("Alice starts at 5", output.contains("moved from 5 to 8"));
        assertTrue("Bob starts at 10", output.contains("moved from 10 to 12"));
    }

    // --- Helpers ---

    static String captureOutput(Runnable r) {
        PrintStream old = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        r.run();
        System.setOut(old);
        return baos.toString();
    }

    static void assertEqual(String name, Object expected, Object actual) {
        if (expected.equals(actual)) { passed++; System.out.println("  PASS: " + name); }
        else { failed++; System.out.println("  FAIL: " + name + " (expected=" + expected + ", actual=" + actual + ")"); }
    }

    static void assertTrue(String name, boolean cond) {
        if (cond) { passed++; System.out.println("  PASS: " + name); }
        else { failed++; System.out.println("  FAIL: " + name + " (expected true)"); }
    }
}
