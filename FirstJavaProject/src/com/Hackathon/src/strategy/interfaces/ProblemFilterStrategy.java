package com.Hackathon.src.strategy.interfaces;

import com.Hackathon.src.model.Problem;

import java.util.List;

public interface ProblemFilterStrategy {
    List<Problem> filter(List<Problem> problems);
}
