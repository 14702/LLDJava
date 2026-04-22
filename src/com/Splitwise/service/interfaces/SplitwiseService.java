package com.Splitwise.service.interfaces;

import com.Splitwise.enums.ExpenseType;
import com.Splitwise.model.User;
import java.util.Map;

public interface SplitwiseService {
    void addUser(User user);
    void addExpense(ExpenseType type, int amount, int paidBy, Map<Integer, Integer> params);
    Map<Integer, Integer> getBalancesForUser(int userId);
    void printBalances();
}
