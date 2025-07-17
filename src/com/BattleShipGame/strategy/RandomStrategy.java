package com.BattleShipGame.strategy;
import java.util.Random;

public class RandomStrategy {
    private final Random random = new Random();

    public int[] nextShot(int boardSize) {
        int x = random.nextInt(boardSize);
        int y = random.nextInt(boardSize);
        return new int[] { x, y };
    }
}
