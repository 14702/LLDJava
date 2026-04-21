package com.Hackathon.src.strategy.interfaces;

import com.Hackathon.src.model.Problem;

import java.util.List;
import java.util.Set;

public interface RecommendationStrategy {
    List<Problem> recommend(Problem solvedProblem, List<Problem> allProblems, Set<String> alreadySolved, int limit);
}