package com.SnakeAndLadder.strategy.impl;

import com.SnakeAndLadder.model.Dice;
import com.SnakeAndLadder.strategy.interfaces.Strategy;

public class SumStrategy implements Strategy {

    Dice dice;
    public SumStrategy(Dice dice) {
        this.dice = dice;
    }

    @Override
    public Integer getValue(Integer numOfDice) {
        int ans = 0;
        for(int i=0;i<numOfDice;i++) ans += dice.rollDice();
        return ans;
    }
}