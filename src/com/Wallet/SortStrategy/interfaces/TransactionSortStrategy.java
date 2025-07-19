package com.Wallet.SortStrategy.interfaces;

import java.util.List;
import com.Wallet.enums.Ordering;
import com.Wallet.model.Transaction;

public interface TransactionSortStrategy {
    void sort(List<Transaction> transactions, Ordering ordering);
}