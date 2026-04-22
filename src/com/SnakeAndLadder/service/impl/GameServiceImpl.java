package com.SnakeAndLadder.service.impl;

import com.SnakeAndLadder.enums.ElementType;
import com.SnakeAndLadder.enums.PlayerStatus;
import com.SnakeAndLadder.model.*;
import com.SnakeAndLadder.service.interfaces.GameService;
import com.SnakeAndLadder.strategy.interfaces.Strategy;

import java.util.*;

public class GameServiceImpl implements GameService {
    private Board board;
    private int size;
    private Integer numOfDice;
    private List<Player> players = new ArrayList<>();
    private Strategy strategy;
    private int[] manualDice;
    private int manualIndex = 0;

    @Override
    public Boolean addPlayer(String name, int startPosition) {
        players.add(new Player(players.size() + 1, name, startPosition, PlayerStatus.PENDING));
        return true;
    }

    @Override
    public Boolean addBoardSize(Integer size) {
        this.size = size;
        List<Cell> cellList = new ArrayList<>();
        for (int i = 0; i <= size; i++)
            cellList.add(new Cell());
        board = new Board(cellList);
        return true;
    }

    @Override
    public Boolean addElements(Integer start, Integer end, ElementType type) {
        board.getCells().get(start).setMove(start, end, type);
        return true;
    }

    @Override
    public Boolean numOfDice(Integer dices) {
        numOfDice = dices;
        return true;
    }

    @Override
    public void addStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void setManualDice(int[] diceValues) {
        this.manualDice = diceValues;
        this.manualIndex = 0;
    }

    private int rollDice() {
        if (manualDice != null && manualIndex < manualDice.length) {
            return manualDice[manualIndex++];
        }
        return strategy.getValue(numOfDice);
    }

    @Override
    public Boolean beginGame() {
        Queue<Player> queue = new LinkedList<>(players);

        while (queue.size() > 1) {
            Player player = queue.poll();

            if (player.getSkipTurns() > 0) {
                System.out.println(player.getName() + " is stuck in a mine, skipping turn ("
                        + player.getSkipTurns() + " left)");
                player.setSkipTurns(player.getSkipTurns() - 1);
                queue.add(player);
                continue;
            }

            int value = rollDice();
            int oldPos = player.getPosition();
            int newPos = oldPos + value;

            if (newPos > size) {
                System.out.println(player.getName() + " rolled a " + value
                        + " and moved from " + oldPos + " to " + oldPos);
                queue.add(player);
                continue;
            }

            Move move = board.getCells().get(newPos).getMove();
            if (move != null) {
                ElementType type = move.getElementType();
                int dest = move.getPos();

                switch (type) {
                    case SNAKE:
                        System.out.println(player.getName() + " rolled a " + value
                                + " and bitten by snake at " + newPos
                                + " and moved from " + newPos + " to " + dest);
                        newPos = dest;
                        break;
                    case LADDER:
                        System.out.println(player.getName() + " rolled a " + value
                                + " and climbed the ladder at " + newPos
                                + " and moved from " + newPos + " to " + dest);
                        newPos = dest;
                        break;
                    case CROCODILE:
                        System.out.println(player.getName() + " rolled a " + value
                                + " and bitten by crocodile at " + newPos
                                + " and moved from " + newPos + " to " + dest);
                        newPos = dest;
                        break;
                    case MINE:
                        System.out.println(player.getName() + " rolled a " + value
                                + " and hit a mine at " + newPos
                                + " and will skip 2 turns");
                        player.setSkipTurns(2);
                        break;
                }

                // chain: destination could have another snake/ladder
                while (board.getCells().get(newPos).getMove() != null) {
                    Move chainMove = board.getCells().get(newPos).getMove();
                    ElementType chainType = chainMove.getElementType();
                    int chainDest = chainMove.getPos();
                    if (chainType == ElementType.MINE) {
                        player.setSkipTurns(2);
                        System.out.println("  chained into mine at " + newPos + ", skip 2 turns");
                        break;
                    }
                    System.out.println("  chained " + chainType + " at " + newPos + " to " + chainDest);
                    newPos = chainDest;
                }
            } else {
                System.out.println(player.getName() + " rolled a " + value
                        + " and moved from " + oldPos + " to " + newPos);
            }

            player.setPosition(newPos);

            if (newPos == size) {
                player.setStatus(PlayerStatus.WIN);
                System.out.println(player.getName() + " wins!");
            } else {
                queue.add(player);
            }
        }

        if (!queue.isEmpty()) {
            Player last = queue.poll();
            last.setStatus(PlayerStatus.LOSE);
        }

        System.out.println("\n=== Final Status ===");
        for (Player p : players) {
            System.out.println(p);
        }
        return true;
    }
}
