package com.BattleShipGame;
import com.BattleShipGame.service.GameServiceImpl;
import com.BattleShipGame.strategy.impl.RandomStrategy;
import com.BattleShipGame.strategy.interfaces.Strategy;

public class Main {
    public static void main(String[] args) {
        // example size
        int size = 8;
        Strategy strategy = new RandomStrategy();
        GameServiceImpl gameService = new GameServiceImpl(strategy);

        gameService.initGame(size);

        // Considering origin at left top. Right side is +X and bottom down +Y
        // add out of board exceptions for ships
        // add ships for PlayerA (left half, columns 0 to 3)
        gameService.addShip("A", 0, 0, 2); // SH1 (at 0,0, size 2, covers [0,0] to [1,1])
        gameService.addShip("A", 2, 2, 2); // SH2 (at 2,2, size 2, covers [2,2] to [3,3])

        // Add ships for PlayerB (right half, columns 4 to 7)
        gameService.addShip("B", 4, 1, 2); // SH1 (at 4,1, size 2, covers [4,1] to [5,2])
        gameService.addShip("B", 6, 4, 2); // SH2 (at 6,4, size 2, covers [6,4] to [7,5])

        System.out.println("\nInitial battlefield ship placement:");
        gameService.viewBattleField();

        System.out.println("\nStarting Game...\n");
        gameService.startGame();

        System.out.println("\nFinal Battlefield ship placement:");
        gameService.viewBattleField();
    }
}
