package com.PendencySystem.repository.impl;

import com.PendencySystem.exceptions.DuplicateEntityException;
import com.PendencySystem.exceptions.NotFoundException;
import com.PendencySystem.model.Transaction;
import com.PendencySystem.repository.interfaces.TransactionRepository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionRepositoryImpl implements TransactionRepository {

    private final ConcurrentHashMap<Integer, Transaction> transactions = new ConcurrentHashMap<>();

    @Override
    public void create(Integer transactionId, List<String> allTags) {
        Transaction transaction = new Transaction(transactionId, allTags);
        if (transactions.putIfAbsent(transactionId, transaction) != null) {
            throw new DuplicateEntityException("Entity already tracked: " + transactionId);
        }
    }

    @Override
    public Transaction get(Integer id) {
        Transaction txn = transactions.get(id);
        if (txn == null) {
            throw new NotFoundException("Entity not found: " + id);
        }
        return txn;
    }

    @Override
    public Transaction remove(Integer id) {
        Transaction txn = transactions.remove(id);
        if (txn == null) {
            throw new NotFoundException("Entity not found: " + id);
        }
        return txn;
    }
}
