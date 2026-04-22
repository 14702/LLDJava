package com.BattleShipGame;

import com.BattleShipGame.service.interfaces.GameService;
import com.BattleShipGame.service.impl.GameServiceImpl;
import com.BattleShipGame.strategy.interfaces.Strategy;
import com.BattleShipGame.strategy.impl.RandomStrategy;

public class Main {
    public static void main(String[] args) {
        int size = 8;
        Strategy strategyA = new RandomStrategy();
        Strategy strategyB = new RandomStrategy();
        GameService gameService = new GameServiceImpl(strategyA, strategyB);

        gameService.initGame(size);

        gameService.addShip("A", 0, 0, 2);
        gameService.addShip("A", 2, 2, 2);

        gameService.addShip("B", 4, 1, 2);
        gameService.addShip("B", 6, 4, 2);

        System.out.println("\nInitial battlefield:");
        gameService.viewBattleField();

        System.out.println("\nStarting Game...\n");
        gameService.startGame();

        System.out.println("\nFinal battlefield:");
        gameService.viewBattleField();
    }
}
