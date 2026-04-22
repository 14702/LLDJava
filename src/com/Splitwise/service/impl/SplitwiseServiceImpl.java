package com.Splitwise.service.impl;

import com.Splitwise.enums.ExpenseType;
import com.Splitwise.model.Expense;
import com.Splitwise.model.User;
import com.Splitwise.service.interfaces.SplitwiseService;
import com.Splitwise.split.interfaces.SplitStrategy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SplitwiseServiceImpl implements SplitwiseService {

    private final ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> balances = new ConcurrentHashMap<>();
    private final List<Expense> expenses = Collections.synchronizedList(new ArrayList<>());
    private final Map<ExpenseType, SplitStrategy> strategies;

    public SplitwiseServiceImpl(Map<ExpenseType, SplitStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public void addUser(User user) {
        users.put(user.getId(), user);
        balances.putIfAbsent(user.getId(), new ConcurrentHashMap<>());
    }

    @Override
    public synchronized void addExpense(ExpenseType type, int amount, int paidBy, Map<Integer, Integer> params) {
        SplitStrategy strategy = strategies.get(type);
        if (strategy == null) throw new IllegalArgumentException("No strategy for type: " + type);

        List<Integer> participants = new ArrayList<>(
                type == ExpenseType.EQUAL ? users.keySet() : params.keySet());

        Map<Integer, Integer> splits = strategy.split(amount, paidBy, participants, params);

        for (Map.Entry<Integer, Integer> e : splits.entrySet()) {
            int owerId = e.getKey();
            int owedAmount = e.getValue();
            balances.computeIfAbsent(paidBy, k -> new ConcurrentHashMap<>())
                    .merge(owerId, owedAmount, Integer::sum);
            balances.computeIfAbsent(owerId, k -> new ConcurrentHashMap<>())
                    .merge(paidBy, -owedAmount, Integer::sum);
        }

        expenses.add(new Expense(amount, paidBy, type, splits));
    }

    @Override
    public Map<Integer, Integer> getBalancesForUser(int userId) {
        return balances.getOrDefault(userId, new ConcurrentHashMap<>());
    }

    @Override
    public void printBalances() {
        for (Map.Entry<Integer, ConcurrentHashMap<Integer, Integer>> entry : balances.entrySet()) {
            int userId = entry.getKey();
            String name = users.containsKey(userId) ? users.get(userId).getName() : "User#" + userId;
            for (Map.Entry<Integer, Integer> debt : entry.getValue().entrySet()) {
                int amt = debt.getValue();
                if (amt > 0) {
                    String otherName = users.containsKey(debt.getKey()) ? users.get(debt.getKey()).getName() : "User#" + debt.getKey();
                    System.out.println("  " + otherName + " owes " + name + " : " + amt);
                }
            }
        }
    }
}
