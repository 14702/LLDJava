package com.Splitwise.split.impl;

import com.Splitwise.split.interfaces.SplitStrategy;
import java.util.*;

public class EqualSplitStrategy implements SplitStrategy {
    @Override
    public Map<Integer, Integer> split(int amount, int paidBy, List<Integer> participants, Map<Integer, Integer> params) {
        int share = amount / participants.size();
        Map<Integer, Integer> splits = new HashMap<>();
        for (int uid : participants) {
            if (uid != paidBy) {
                splits.put(uid, share);
            }
        }
        return splits;
    }
}
