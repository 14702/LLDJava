package com.Hackathon.src.strategy.impl;

import com.Hackathon.src.model.Problem;
import com.Hackathon.src.strategy.interfaces.ScoringStrategy;

public class TimeWeightedScoringStrategy implements ScoringStrategy {
    private final long bonusThresholdMs;
    private final double bonusMultiplier;

    public TimeWeightedScoringStrategy(long bonusThresholdMs, double bonusMultiplier) {
        this.bonusThresholdMs = bonusThresholdMs;
        this.bonusMultiplier = bonusMultiplier;
    }

    @Override
    public int calculateScore(Problem problem, long timeTakenMs) {
        int base = problem.getScore();
        if (timeTakenMs <= bonusThresholdMs) {
            return (int) (base * bonusMultiplier);
        }
        return base;
    }
}
