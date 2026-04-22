package com.MiniWallet.filter.interfaces;

import com.MiniWallet.model.Transaction;

import java.util.List;

public interface TransactionFilter {
    List<Transaction> filter(List<Transaction> transactions);
}
