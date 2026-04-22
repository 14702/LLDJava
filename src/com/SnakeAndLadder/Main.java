package com.SnakeAndLadder;

import com.SnakeAndLadder.enums.ElementType;
import com.SnakeAndLadder.model.Dice;
import com.SnakeAndLadder.service.impl.GameServiceImpl;
import com.SnakeAndLadder.service.interfaces.GameService;
import com.SnakeAndLadder.strategy.impl.SumStrategy;

import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        GameService gameService = new GameServiceImpl();
        gameService.addBoardSize(100);
        gameService.numOfDice(1);
        gameService.addStrategy(new SumStrategy(new Dice()));

        try (BufferedReader br = new BufferedReader(new FileReader("src/com/SnakeAndLadder/input.txt"))) {
            // Snakes
            int snakeCount = Integer.parseInt(br.readLine().trim());
            for (int i = 0; i < snakeCount; i++) {
                String[] parts = br.readLine().trim().split(" ");
                gameService.addElements(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), ElementType.SNAKE);
            }

            // Ladders
            int ladderCount = Integer.parseInt(br.readLine().trim());
            for (int i = 0; i < ladderCount; i++) {
                String[] parts = br.readLine().trim().split(" ");
                gameService.addElements(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), ElementType.LADDER);
            }

            // Optional: Crocodiles and Mines
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("CROCODILE")) {
                    int count = Integer.parseInt(line.split(" ")[1]);
                    for (int i = 0; i < count; i++) {
                        String[] parts = br.readLine().trim().split(" ");
                        gameService.addElements(Integer.parseInt(parts[0]), 0, ElementType.CROCODILE);
                    }
                } else if (line.startsWith("MINE")) {
                    int count = Integer.parseInt(line.split(" ")[1]);
                    for (int i = 0; i < count; i++) {
                        String[] parts = br.readLine().trim().split(" ");
                        gameService.addElements(Integer.parseInt(parts[0]), 0, ElementType.MINE);
                    }
                } else {
                    // Player count line
                    int playerCount = Integer.parseInt(line.split(" ")[0]);
                    for (int i = 0; i < playerCount; i++) {
                        String[] parts = br.readLine().trim().split(" ");
                        String name = parts[0];
                        int startPos = Integer.parseInt(parts[1]);
                        gameService.addPlayer(name, startPos);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Manual die override example (comment out for random):
        // gameService.setManualDice(new int[]{6, 1, 6, 4, 4, 6, 5, 4, 1, 6});

        gameService.beginGame();
    }
}
