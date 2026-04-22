package com.Splitwise.split.impl;

import com.Splitwise.split.interfaces.SplitStrategy;
import java.util.*;

public class PercentageSplitStrategy implements SplitStrategy {
    @Override
    public Map<Integer, Integer> split(int amount, int paidBy, List<Integer> participants, Map<Integer, Integer> params) {
        int totalPercent = params.values().stream().mapToInt(Integer::intValue).sum();
        if (totalPercent != 100) {
            throw new IllegalArgumentException("Percentages must sum to 100, got: " + totalPercent);
        }
        Map<Integer, Integer> splits = new HashMap<>();
        for (Map.Entry<Integer, Integer> e : params.entrySet()) {
            if (e.getKey() != paidBy) {
                splits.put(e.getKey(), amount * e.getValue() / 100);
            }
        }
        return splits;
    }
}
