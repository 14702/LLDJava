package com.Splitwise;
import java.util.List;

public class EqualExpense extends Expense{

    EqualExpense(int id, int amount, int paidBy, List<Split> splits){
        super(id, amount, paidBy, splits);
    }

    public boolean validate(){

        return false;
    }
}
