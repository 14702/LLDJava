package com.Splitwise;
import java.util.*;
import java.util.List;

public class ExpenseController {
    List<Expense> expenses;
    Map<Integer, User> userMap;             // userid , User
    public Map<Integer, Map<Integer, Integer>> balanceSheet; // userid, {friend userid , money owe}

    ExpenseController(){
        this.expenses = new ArrayList<>();
        this.userMap = new HashMap<>();
        this.balanceSheet = new HashMap<>();
    }

    public void addUser(User user){
        if(!userMap.containsKey(user.id)){
            userMap.put(user.id, user);
            balanceSheet.put(user.id, new HashMap<>());
        }
    }

    public void addExpense(ExpenseType expenseType, int id, int amount, int paidBy){
        Expense expense;

        if(expenseType == ExpenseType.EQUAL){
            List<Split> splits = new ArrayList<>();
            // updating balance sheet

            for(int i = 1 ; i <= 3; i++){

                if(userMap.containsKey(paidBy)){
                    System.out.println("Updating Balance sheet for Payer  "+ i + " amount " + amount/3 );

                    for(int j = 1 ; j <= 3 ; j++){
                        if(j == paidBy)
                            balanceSheet.get(paidBy).put(paidBy, 0); // self 0
                        else
                            balanceSheet.get(paidBy).put(j, amount/3); // rest split
                    }
                } else{
                    balanceSheet.get(i).put(paidBy, -amount/3);
                }
            }
            expense = new EqualExpense(id,amount, paidBy, splits);
            expenses.add(expense);

            // update balance sheet of all the users.
        }//......... expand

    }


}
