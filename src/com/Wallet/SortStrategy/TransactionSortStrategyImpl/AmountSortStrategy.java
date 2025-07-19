package com.Wallet.SortStrategy.TransactionSortStrategyImpl;

import java.util.List;
import com.Wallet.SortStrategy.interfaces.TransactionSortStrategy;
import com.Wallet.enums.Ordering;
import com.Wallet.model.Transaction;

public class AmountSortStrategy implements TransactionSortStrategy {
    @Override
    public void sort(List<Transaction> transactions, Ordering ordering) {
        if(ordering == Ordering.ASC){
            transactions.sort((t1, t2) -> Double.compare(t1.getAmount(), t2.getAmount()));
        } else {
            transactions.sort((t1, t2) -> -1 * Double.compare(t1.getAmount(), t2.getAmount()));
        }
    }
}