package com.MiniWallet.offer.impl;

import com.MiniWallet.model.Transaction;
import com.MiniWallet.model.Wallet;
import com.MiniWallet.offer.interfaces.Offer;

public class EqualBalanceOffer implements Offer {

    @Override
    public boolean isEligible(Transaction transaction, Wallet senderWallet, Wallet receiverWallet) {
        return Math.abs(senderWallet.getBalance() - receiverWallet.getBalance()) < 0.01;
    }

    @Override
    public double calculateCashback(Transaction transaction, Wallet senderWallet, Wallet receiverWallet) {
        return transaction.getAmount() * 0.05;
    }

    @Override
    public String getName() { return "Equal balance after transfer → 5% cashback"; }
}
