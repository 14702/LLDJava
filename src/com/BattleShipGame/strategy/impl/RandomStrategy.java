package com.BattleShipGame.strategy.impl;
import com.BattleShipGame.strategy.interfaces.Strategy;

import java.util.Random;

public class RandomStrategy implements Strategy {
    private final Random random = new Random();

    @Override
    public int[] nextShot(int boardSize, String player) {
        int x = random.nextInt(boardSize);
        int y = random.nextInt(boardSize);
        return new int[] { x, y };
    }

    @Override
    public int randomInRange(int min, int max) {
        return min + random.nextInt(max - min);
    }
}
