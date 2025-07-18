package com.Wallet.SortStrategy.TransactionSortStrategyImpl;

import java.util.List;
import com.Wallet.SortStrategy.TransactionSortStrategy;
import com.Wallet.enums.Ordering;
import com.Wallet.model.Transaction;

public class TimeSortStrategy implements TransactionSortStrategy {
    @Override
    public void sort(List<Transaction> transactions, Ordering ordering) {
        if(ordering == Ordering.ASC){
            transactions.sort((t1, t2) -> t1.getTimestamp().compareTo(t2.getTimestamp()));
        } else {
            transactions.sort((t1, t2) -> -1 * t1.getTimestamp().compareTo(t2.getTimestamp()));
        }

    }
}