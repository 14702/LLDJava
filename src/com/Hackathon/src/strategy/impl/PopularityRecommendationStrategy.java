package com.Hackathon.src.strategy.impl;

import com.Hackathon.src.model.Problem;
import com.Hackathon.src.strategy.interfaces.RecommendationStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PopularityRecommendationStrategy implements RecommendationStrategy {
    @Override
    public List<Problem> recommend(Problem solved, List<Problem> all, Set<String> alreadySolved, int limit) {
        return all.stream()
                .filter(p -> !alreadySolved.contains(p.getId()))
                .sorted(Comparator.comparingInt(Problem::getLikes).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
