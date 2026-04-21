package com.Hackathon.src.strategy.impl;

import com.Hackathon.src.model.Problem;
import com.Hackathon.src.strategy.interfaces.ScoringStrategy;

public class StandardScoringStrategy implements ScoringStrategy {
    @Override
    public int calculateScore(Problem problem, long timeTakenMs) {
        return problem.getScore();
    }
}
