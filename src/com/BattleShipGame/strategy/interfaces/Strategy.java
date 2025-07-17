package com.BattleShipGame.strategy.interfaces;
public interface Strategy {

    int[] nextShot(int boardSize, String player);
    int randomInRange(int min, int max);
}