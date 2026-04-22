package com.MiniWallet.filter.impl;

import com.MiniWallet.enums.TransactionType;
import com.MiniWallet.filter.interfaces.TransactionFilter;
import com.MiniWallet.model.Transaction;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionTypeFilter implements TransactionFilter {
    private final TransactionType type;

    public TransactionTypeFilter(TransactionType type) {
        this.type = type;
    }

    @Override
    public List<Transaction> filter(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .collect(Collectors.toList());
    }
}
