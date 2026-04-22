package com.BattleShipGame.strategy.impl;

import com.BattleShipGame.strategy.interfaces.Strategy;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class RandomStrategy implements Strategy {
    private final Random random = new Random();
    private final Set<String> firedCoordinates = new ConcurrentSkipListSet<>();

    @Override
    public int[] nextShot(int minCol, int maxColExclusive, int minRow, int maxRowExclusive) {
        int totalCells = (maxColExclusive - minCol) * (maxRowExclusive - minRow);
        if (firedCoordinates.size() >= totalCells) return null;

        int x, y;
        do {
            x = minCol + random.nextInt(maxColExclusive - minCol);
            y = minRow + random.nextInt(maxRowExclusive - minRow);
        } while (firedCoordinates.contains(x + "," + y));

        firedCoordinates.add(x + "," + y);
        return new int[]{x, y};
    }
}
