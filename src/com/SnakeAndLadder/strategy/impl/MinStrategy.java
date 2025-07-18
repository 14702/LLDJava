package com.SnakeAndLadder.strategy.impl;

import com.SnakeAndLadder.model.Dice;
import com.SnakeAndLadder.strategy.interfaces.Strategy;

public class MinStrategy implements Strategy {
    Dice dice;

    public MinStrategy(Dice dice) {
        this.dice = dice;
    }

    @Override
    public Integer getValue(Integer numOfDice) {
        int ans = Integer.MAX_VALUE;
        for(int i=0;i<numOfDice;i++) ans = Math.min(ans, dice.rollDice());
        return ans;
    }
}