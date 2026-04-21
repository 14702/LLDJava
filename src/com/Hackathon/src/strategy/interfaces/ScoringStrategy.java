package com.Hackathon.src.strategy.interfaces;

import com.Hackathon.src.model.Problem;

public interface ScoringStrategy {
    int calculateScore(Problem problem, long timeTakenMs);
}