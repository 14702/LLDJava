package com.MiniWallet.filter.impl;

import com.MiniWallet.filter.interfaces.TransactionFilter;
import com.MiniWallet.model.Transaction;

import java.util.List;
import java.util.stream.Collectors;

public class UserFilter implements TransactionFilter {
    private final String userId;

    public UserFilter(String userId) {
        this.userId = userId;
    }

    @Override
    public List<Transaction> filter(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getSenderId().equals(userId) || t.getReceiverId().equals(userId))
                .collect(Collectors.toList());
    }
}
