package com.SnakeAndLadder;

import com.SnakeAndLadder.model.Dice;
import com.SnakeAndLadder.enums.ElementType;
import com.SnakeAndLadder.service.interfaces.GameService;
import com.SnakeAndLadder.service.impl.GameServiceImpl;
import com.SnakeAndLadder.strategy.impl.MaxStrategy;
import com.SnakeAndLadder.strategy.impl.MinStrategy;
import com.SnakeAndLadder.strategy.impl.SumStrategy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        GameService gameService = new GameServiceImpl();


        try(BufferedReader br = new BufferedReader(new FileReader("src/com/SnakeAndLadder/input.txt"))) {

            String line = br.readLine();
            while (line!= null) {
                System.out.println(line);
                String[] inputs = line.split(" ");

                String action = inputs[0];
                if (action.equals("PLAYER")) {

                    System.out.println(gameService.addPlayers(Integer.parseInt(inputs[1])));

                } else if(action.equals("BOARD_SIZE")) {

                    System.out.println(gameService.addBoardSize(Integer.parseInt(inputs[1])));

                } else if(action.equals("NUM_DICE")) {

                    System.out.println(gameService.numOfDice(Integer.parseInt(inputs[1])));

                } else if (action.equals("MOVEMENT_STRATEGY")) {
                    switch (inputs[1]) {
                        case "SUM":
                            gameService.addStrategy(new SumStrategy(new Dice()));
                            break;
                        case "MAX":
                            gameService.addStrategy(new MaxStrategy(new Dice()));
                            break;
                        case "MIN":
                            gameService.addStrategy(new MinStrategy(new Dice()));
                            break;
                    }

                } else if (action.equals("SNAKE") || action.equals("LADDER")) {
                    Integer num = Integer.parseInt(inputs[1]);
                    for (int i=0;i< num;i++) {
                        String line1 = br.readLine();
                        System.out.println(line1);
                        String[] points = line1.split(" ");
                        gameService.addElements(Integer.parseInt(points[0]), Integer.parseInt(points[1]),
                                ElementType.valueOf(action));
                    }
                }

                line = br.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        gameService.beginGame();
    }
}
