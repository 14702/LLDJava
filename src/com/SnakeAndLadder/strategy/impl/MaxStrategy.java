package com.SnakeAndLadder.strategy.impl;

import com.SnakeAndLadder.model.Dice;
import com.SnakeAndLadder.strategy.interfaces.Strategy;

public class MaxStrategy implements Strategy {
    Dice dice;

    public MaxStrategy(Dice dice) {
        this.dice = dice;
    }

    @Override
    public Integer getValue(Integer numOfDice) {
        int ans = Integer.MIN_VALUE;
        for(int i=0;i<numOfDice;i++) ans = Math.max(ans, dice.rollDice());
        return ans;
    }
}