package com.Splitwise.split.interfaces;

import java.util.List;
import java.util.Map;

public interface SplitStrategy {
    Map<Integer, Integer> split(int amount, int paidBy, List<Integer> participants, Map<Integer, Integer> params);
}
