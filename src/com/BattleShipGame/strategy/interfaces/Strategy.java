package com.BattleShipGame.strategy.interfaces;

public interface Strategy {
    int[] nextShot(int minCol, int maxColExclusive, int minRow, int maxRowExclusive);
}