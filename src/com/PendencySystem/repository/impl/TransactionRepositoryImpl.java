package com.PendencySystem.repository.impl;

import com.PendencySystem.exceptions.DuplicateEntityException;
import com.PendencySystem.exceptions.NotFoundException;
import com.PendencySystem.model.Transaction;
import com.PendencySystem.repository.interfaces.TransactionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionRepositoryImpl implements TransactionRepository {

    private static volatile TransactionRepositoryImpl instance;
    private static Map<Integer, Transaction> transactions;

    private TransactionRepositoryImpl(){
        transactions = new HashMap<>();
    }

    public static TransactionRepositoryImpl getInstance(){
        if(instance == null){
            synchronized (TransactionRepositoryImpl.class){
                if(instance == null){
                    instance = new TransactionRepositoryImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public Transaction create(Integer transactionId, List<String> allTags){
        if(transactions.containsKey(transactionId)){
            throw new DuplicateEntityException("Transaction already exists");
        }
        System.out.println("Start tracking the Tags for Id "+ transactionId + "  +++++++++++++++++++++++++++++++++++");
        Transaction transaction = new Transaction(transactionId, allTags);
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    @Override
    public Transaction get(Integer id){
        if(!transactions.containsKey(id)){
            throw new NotFoundException("Transaction not found");
        }
        return transactions.get(id);
    }

    @Override
    public void remove(Integer id){
        if(!transactions.containsKey(id)){
            throw new NotFoundException("Transaction not found");
        }
        System.out.println("Stop tracking the Tags for Id "+ id + "   -----------------------------------");
        transactions.remove(id);
    }
}