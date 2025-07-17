package com.BattleShipGame.service.interfaces;

public interface GameService {
    void initGame(int size);
    boolean addShip(String player, int x, int y, int shipSize);
    void viewBattleField();
    void startGame();
}