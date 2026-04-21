package com.Hackathon.src.strategy.impl;

import com.Hackathon.src.model.Problem;
import com.Hackathon.src.strategy.interfaces.ProblemFilterStrategy;

import java.util.List;
import java.util.stream.Collectors;

public class TagFilterStrategyImpl implements ProblemFilterStrategy {
    private final String tag;

    public TagFilterStrategyImpl(String tag) {
        this.tag = tag;
    }

    @Override
    public List<Problem> filter(List<Problem> problems) {
        return problems.stream()
                .filter(p -> p.getTag().equalsIgnoreCase(tag))
                .collect(Collectors.toList());
    }

    public String getTag() { return tag; }
}