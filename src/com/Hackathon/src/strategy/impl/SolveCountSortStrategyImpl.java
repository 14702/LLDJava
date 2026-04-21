package com.Hackathon.src.strategy.impl;

import com.Hackathon.src.model.Problem;
import com.Hackathon.src.strategy.interfaces.ProblemSortStrategy;

import java.util.Comparator;
import java.util.List;

public class SolveCountSortStrategyImpl implements ProblemSortStrategy {
    @Override
    public void sort(List<Problem> problems) {
        problems.sort(Comparator.comparingInt(Problem::getSolvedCount).reversed());
    }
}
