package com.SnakeAndLadder.service.impl;

import com.SnakeAndLadder.enums.ElementType;
import com.SnakeAndLadder.enums.PlayerStatus;
import com.SnakeAndLadder.model.Board;
import com.SnakeAndLadder.model.*;
import com.SnakeAndLadder.service.interfaces.GameService;
import com.SnakeAndLadder.strategy.interfaces.Strategy;

import java.util.*;

public class GameServiceImpl implements GameService {
    private Board board;
    int size;
    private Integer numOfDice;
    private Map<Integer, Player> res = new HashMap<>();
    private int winnerCount;
    private Strategy strategy;



    @Override
    public Boolean addPlayers(Integer numOfPlayers) {

        for(int i=1;i<=numOfPlayers;i++) {
            res.putIfAbsent(i, new Player(i, 0, PlayerStatus.PENDING));
        }
        return true;
    }

    @Override
    public Boolean addBoardSize(Integer size) {
        this.size = size;
        List<Cell> cellList = new ArrayList<>();
        for(int i=0;i<=size;i++)
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
    public Boolean beginGame() {
        Queue<Player> que = new LinkedList<>();
        que.addAll(res.values());
        while (que.size() > 1) {
            Player player = que.poll();
            System.out.println("Player "+ player.getId() + " current position: "+ player.getPosition());
            Integer value = strategy.getValue(numOfDice);
            System.out.println("Dice is rolled and the value is = "+value);
            int pos = player.getPosition() + value;
            if (pos > size) {
                System.out.println("Player "+ player.getId() + " can not move out of the box from "+ player.getPosition() +" to "+ pos);
                que.add(player);
            } else {
                while (board.getCells().get(pos).getMove() != null) {
                    System.out.println("Player "+player.getId()+" got "+board.getCells().get(pos).getMove().getElementType()
                            +" from : " + pos + " end :" + board.getCells().get(pos).getMove().getPos());
                    pos = board.getCells().get(pos).getMove().getPos();
                }

                if (pos == size) {
                    winnerCount++;
                    player.setPosition(pos);
                    player.setStatus(PlayerStatus.WIN);
                    System.out.println("Player "+ player.getId() + " : Won.");
                } else {
                    System.out.println("Player "+ player.getId() + " moving from = "+ player.getPosition() +" to = "+ pos);
                    player.setPosition(pos);
                    que.add(player);
                }

            }

        }
        Player player = que.poll();
        player.setStatus(PlayerStatus.LOSE);
        System.out.println("All players status : ");
        System.out.println(res.values());
        return true;
    }

    @Override
    public void addStrategy(Strategy strategy) {
        this.strategy = strategy;
    }


}