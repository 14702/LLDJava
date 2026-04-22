package com.MiniWallet.service.impl;

import com.MiniWallet.filter.interfaces.TransactionFilter;
import com.MiniWallet.model.Transaction;
import com.MiniWallet.service.interfaces.TransactionService;
import com.MiniWallet.sorter.interfaces.TransactionSorter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class TransactionServiceImpl implements TransactionService {
    private final ConcurrentHashMap<String, List<Transaction>> userTransactions = new ConcurrentHashMap<>();

    @Override
    public void addTransaction(Transaction transaction) {
        userTransactions.computeIfAbsent(transaction.getSenderId(),
                k -> new CopyOnWriteArrayList<>()).add(transaction);
        if (!transaction.getSenderId().equals(transaction.getReceiverId())) {
            userTransactions.computeIfAbsent(transaction.getReceiverId(),
                    k -> new CopyOnWriteArrayList<>()).add(transaction);
        }
    }

    @Override
    public List<Transaction> getTransactions(String userId, TransactionFilter filter, TransactionSorter sorter) {
        List<Transaction> txns = userTransactions.getOrDefault(userId, new ArrayList<>());
        List<Transaction> result = new ArrayList<>(txns);
        if (filter != null) {
            result = filter.filter(result);
        }
        if (sorter != null) {
            result = sorter.sort(result);
        }
        return result;
    }
}
