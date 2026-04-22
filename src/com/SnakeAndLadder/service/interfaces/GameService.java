package com.SnakeAndLadder.service.interfaces;

import com.SnakeAndLadder.enums.ElementType;
import com.SnakeAndLadder.strategy.interfaces.Strategy;

public interface GameService {

    Boolean addPlayer(String name, int startPosition);

    Boolean addBoardSize(Integer size);

    Boolean addElements(Integer start, Integer end, ElementType type);

    Boolean numOfDice(Integer dices);

    Boolean beginGame();

    void addStrategy(Strategy strategy);

    void setManualDice(int[] diceValues);
}