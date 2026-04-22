package com.MiniWallet.sorter.impl;

import com.MiniWallet.model.Transaction;
import com.MiniWallet.sorter.interfaces.TransactionSorter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AmountSorter implements TransactionSorter {
    private final boolean ascending;

    public AmountSorter(boolean ascending) {
        this.ascending = ascending;
    }

    @Override
    public List<Transaction> sort(List<Transaction> transactions) {
        List<Transaction> sorted = new ArrayList<>(transactions);
        Comparator<Transaction> cmp = Comparator.comparingDouble(Transaction::getAmount);
        sorted.sort(ascending ? cmp : cmp.reversed());
        return sorted;
    }
}
