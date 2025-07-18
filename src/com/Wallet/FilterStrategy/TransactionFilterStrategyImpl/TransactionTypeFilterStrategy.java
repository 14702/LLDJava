package com.Wallet.FilterStrategy.TransactionFilterStrategyImpl;

import com.Wallet.FilterStrategy.TransactionFilterStrategy;
import com.Wallet.model.Transaction;
import java.util.List;
import java.util.stream.Collectors;
import com.Wallet.enums.TransactionType;

public class TransactionTypeFilterStrategy implements TransactionFilterStrategy {
    TransactionType transactionType;

    public TransactionTypeFilterStrategy(TransactionType transactionType){
        this.transactionType = transactionType;
    }
    @Override
    public List<Transaction> filter(List<Transaction> transactionList) {
        return transactionList.stream().filter(transaction -> transaction.getTransactionType() == transactionType).collect(
                Collectors.toList());
    }
}