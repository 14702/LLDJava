package com.Hackathon.src.strategy.impl;

import com.Hackathon.src.model.Problem;
import com.Hackathon.src.strategy.interfaces.ProblemFilterStrategy;

import java.util.List;

import static com.Hackathon.src.constants.AppConstant.FILTERING_BY_TAG;

public class TagFilterStrategyImpl implements ProblemFilterStrategy {

    private final String tag;

    public TagFilterStrategyImpl(String tag) {
        this.tag = tag;
    }

    @Override
    public List<Problem> filter(List<Problem> problems) {
        System.out.println(FILTERING_BY_TAG + tag);
        return problems.stream()
                .filter(p -> p.getTag().equalsIgnoreCase(tag)).toList();
    }

    public String getTag() {
        return tag;
    }
}
