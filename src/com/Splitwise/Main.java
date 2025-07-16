package com.Splitwise;

public class Main {
    public static void main (String [] args){
        // create users and add the, to expense controller
        // exprensecontroller holds all the data - list<expense>, map <id, User>, map <id, balancesheet ==  userid, amount >
        // when expesnse is added we just update the amount from 2 places, paid by & owed by (get them via map)
        // expense = Sum of Splits (User, amount)
        // expense type is 3 types, equal,exact, %. So create 3 split types and 3 expense type classes
        // We usually make types and similar class types so that we know for which type we have to create which object


        User u1 = new User (1, "u1");
        User u2 = new User (2, "u2");
        User u3 = new User (3, "u3");

        ExpenseController expenseController = new ExpenseController();
        expenseController.addUser(u1);
        expenseController.addUser(u2);
        expenseController.addUser(u3);

        expenseController.addExpense(ExpenseType.EQUAL, 1, 1000, 2);
        System.out.println(expenseController.balanceSheet.get(1).get(2));
    }
}
