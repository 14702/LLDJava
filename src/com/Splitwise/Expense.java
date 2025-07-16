package com.Splitwise;
import java.util.List;
import java.util.ArrayList;

public abstract class Expense {
    int id;
    int amount;
    int paidBy;
    List<Split> splits;

    Expense(int id, int amount, int paidBy, List<Split> splits){
        this.id = id;
        this.amount = amount;
        this.paidBy = paidBy;
        this.splits = new ArrayList<>();
    }

    public abstract boolean validate();
}
