package com.Splitwise;

import com.Splitwise.enums.ExpenseType;
import com.Splitwise.model.User;
import com.Splitwise.service.impl.SplitwiseServiceImpl;
import com.Splitwise.service.interfaces.SplitwiseService;
import com.Splitwise.split.impl.*;
import com.Splitwise.split.interfaces.SplitStrategy;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<ExpenseType, SplitStrategy> strategies = new HashMap<>();
        strategies.put(ExpenseType.EQUAL, new EqualSplitStrategy());
        strategies.put(ExpenseType.EXACT, new ExactSplitStrategy());
        strategies.put(ExpenseType.PERCENTAGE, new PercentageSplitStrategy());

        SplitwiseService service = new SplitwiseServiceImpl(strategies);

        User u1 = new User(1, "Alice");
        User u2 = new User(2, "Bob");
        User u3 = new User(3, "Charlie");
        service.addUser(u1);
        service.addUser(u2);
        service.addUser(u3);

        System.out.println("=== EQUAL SPLIT: Alice pays 900 ===");
        service.addExpense(ExpenseType.EQUAL, 900, 1, null);
        service.printBalances();

        System.out.println("\n=== EXACT SPLIT: Bob pays 1000 (Alice=200, Bob=500, Charlie=300) ===");
        Map<Integer, Integer> exact = new HashMap<>();
        exact.put(1, 200);
        exact.put(2, 500);
        exact.put(3, 300);
        service.addExpense(ExpenseType.EXACT, 1000, 2, exact);
        service.printBalances();

        System.out.println("\n=== PERCENTAGE SPLIT: Charlie pays 600 (Alice=50%, Bob=25%, Charlie=25%) ===");
        Map<Integer, Integer> pct = new HashMap<>();
        pct.put(1, 50);
        pct.put(2, 25);
        pct.put(3, 25);
        service.addExpense(ExpenseType.PERCENTAGE, 600, 3, pct);
        service.printBalances();

        System.out.println("\n=== Alice's balances ===");
        Map<Integer, Integer> aliceBalances = service.getBalancesForUser(1);
        for (Map.Entry<Integer, Integer> e : aliceBalances.entrySet()) {
            System.out.println("  vs User#" + e.getKey() + " : " + e.getValue()
                    + (e.getValue() > 0 ? " (owed to Alice)" : " (Alice owes)"));
        }
    }
}
