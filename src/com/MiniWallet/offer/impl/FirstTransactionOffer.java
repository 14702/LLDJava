package com.MiniWallet.offer.impl;

import com.MiniWallet.model.Transaction;
import com.MiniWallet.model.Wallet;
import com.MiniWallet.offer.interfaces.Offer;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FirstTransactionOffer implements Offer {
    private final Set<String> claimedUsers = ConcurrentHashMap.newKeySet();

    @Override
    public boolean isEligible(Transaction transaction, Wallet senderWallet, Wallet receiverWallet) {
        return transaction.getAmount() > 100 && !claimedUsers.contains(transaction.getSenderId());
    }

    @Override
    public double calculateCashback(Transaction transaction, Wallet senderWallet, Wallet receiverWallet) {
        claimedUsers.add(transaction.getSenderId());
        return transaction.getAmount() * 0.10;
    }

    @Override
    public String getName() { return "First Transaction > ₹100 → 10% cashback"; }
}
