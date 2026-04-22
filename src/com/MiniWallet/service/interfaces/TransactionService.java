package com.MiniWallet.service.interfaces;

import com.MiniWallet.filter.interfaces.TransactionFilter;
import com.MiniWallet.model.Transaction;
import com.MiniWallet.sorter.interfaces.TransactionSorter;

import java.util.List;

public interface TransactionService {
    void addTransaction(Transaction transaction);
    List<Transaction> getTransactions(String userId, TransactionFilter filter, TransactionSorter sorter);
}
