package com.Hackathon.src.strategy.impl;

import com.Hackathon.src.enums.Difficulty;
import com.Hackathon.src.model.Problem;
import com.Hackathon.src.strategy.interfaces.ProblemFilterStrategy;

import java.util.List;

import static com.Hackathon.src.constants.AppConstant.FILTERING_BY_DIFFICULTY;

public class DifficultyFilterStrategyImpl implements ProblemFilterStrategy {

    private final Difficulty difficulty;

    public DifficultyFilterStrategyImpl(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<Problem> filter(List<Problem> problems) {
        System.out.println(FILTERING_BY_DIFFICULTY + difficulty);
        return problems.stream()
                .filter(p -> p.getProblemDifficulty() == difficulty)
                .toList();
    }
}
