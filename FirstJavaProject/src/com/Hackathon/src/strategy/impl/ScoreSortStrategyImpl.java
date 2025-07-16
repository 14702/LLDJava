package com.Hackathon.src.strategy.impl;

import com.Hackathon.src.model.Problem;
import com.Hackathon.src.strategy.interfaces.ProblemSortStrategy;

import java.util.List;

public class ScoreSortStrategyImpl implements ProblemSortStrategy {

    @Override
    public void sort(List<Problem> problems) {
        problems.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));
    }
}
