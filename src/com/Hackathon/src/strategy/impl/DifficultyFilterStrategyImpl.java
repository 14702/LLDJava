package com.Hackathon.src.strategy.impl;

import com.Hackathon.src.enums.Difficulty;
import com.Hackathon.src.model.Problem;
import com.Hackathon.src.strategy.interfaces.ProblemFilterStrategy;

import java.util.List;
import java.util.stream.Collectors;

public class DifficultyFilterStrategyImpl implements ProblemFilterStrategy {
    private final Difficulty difficulty;

    public DifficultyFilterStrategyImpl(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public List<Problem> filter(List<Problem> problems) {
        return problems.stream()
                .filter(p -> p.getDifficulty() == difficulty)
                .collect(Collectors.toList());
    }
}