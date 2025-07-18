package com.Wallet.FilterStrategy;
import com.Wallet.model.Transaction;

import java.util.List;

public interface TransactionFilterStrategy {
    List<Transaction> filter(List<Transaction> transactionList);
}