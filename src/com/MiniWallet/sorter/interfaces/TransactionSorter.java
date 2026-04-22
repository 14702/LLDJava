package com.MiniWallet.sorter.interfaces;

import com.MiniWallet.model.Transaction;

import java.util.List;

public interface TransactionSorter {
    List<Transaction> sort(List<Transaction> transactions);
}
