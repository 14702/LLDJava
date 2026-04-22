package com.Splitwise.model;

import com.Splitwise.enums.ExpenseType;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Expense {
    private static final AtomicInteger ID_GEN = new AtomicInteger(0);

    private final int id;
    private final int amount;
    private final int paidBy;
    private final ExpenseType type;
    private final Map<Integer, Integer> splits;

    public Expense(int amount, int paidBy, ExpenseType type, Map<Integer, Integer> splits) {
        this.id = ID_GEN.incrementAndGet();
        this.amount = amount;
        this.paidBy = paidBy;
        this.type = type;
        this.splits = splits;
    }

    public int getId() { return id; }
    public int getAmount() { return amount; }
    public int getPaidBy() { return paidBy; }
    public ExpenseType getType() { return type; }
    public Map<Integer, Integer> getSplits() { return splits; }
}
