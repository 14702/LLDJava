package com.Splitwise.split.impl;

import com.Splitwise.split.interfaces.SplitStrategy;
import java.util.*;

public class ExactSplitStrategy implements SplitStrategy {
    @Override
    public Map<Integer, Integer> split(int amount, int paidBy, List<Integer> participants, Map<Integer, Integer> params) {
        int total = params.values().stream().mapToInt(Integer::intValue).sum();
        if (total != amount) {
            throw new IllegalArgumentException("Exact split amounts don't add up to total: " + total + " vs " + amount);
        }
        Map<Integer, Integer> splits = new HashMap<>();
        for (Map.Entry<Integer, Integer> e : params.entrySet()) {
            if (e.getKey() != paidBy) {
                splits.put(e.getKey(), e.getValue());
            }
        }
        return splits;
    }
}
