package com.BattleShipGame;
import com.BattleShipGame.model.Ship;
import com.BattleShipGame.service.GameService;
import com.BattleShipGame.strategy.RandomStrategy;

public class BattleShipDemo {
    public static void main(String[] args) {
        int size = 8; // Example size
        GameService gameService = new GameService();

        gameService.initGame(size);

        // Add ships for PlayerA (left half)
        gameService.addShip("A", 0, 0, 2, 2); // SH1
        gameService.addShip("A", 2, 2, 2, 2); // SH2

        // Add ships for PlayerB (right half)
        gameService.addShip("B", 4, 1, 2, 2); // SH1
        gameService.addShip("B", 6, 4, 2, 2); // SH2

        System.out.println("\nInitial Battlefield:");
        gameService.viewBattleField();

        System.out.println("\nStarting Game...\n");
        gameService.startGame();

        System.out.println("\nFinal Battlefield:");
        gameService.viewBattleField();
    }
}
