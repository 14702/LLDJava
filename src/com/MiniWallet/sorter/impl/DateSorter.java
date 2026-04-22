package com.MiniWallet.sorter.impl;

import com.MiniWallet.model.Transaction;
import com.MiniWallet.sorter.interfaces.TransactionSorter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DateSorter implements TransactionSorter {
    private final boolean ascending;

    public DateSorter(boolean ascending) {
        this.ascending = ascending;
    }

    @Override
    public List<Transaction> sort(List<Transaction> transactions) {
        List<Transaction> sorted = new ArrayList<>(transactions);
        Comparator<Transaction> cmp = Comparator.comparing(Transaction::getTimestamp);
        sorted.sort(ascending ? cmp : cmp.reversed());
        return sorted;
    }
}
