package com.MiniWallet.offer.interfaces;

import com.MiniWallet.model.Transaction;
import com.MiniWallet.model.Wallet;

public interface Offer {
    boolean isEligible(Transaction transaction, Wallet senderWallet, Wallet receiverWallet);
    double calculateCashback(Transaction transaction, Wallet senderWallet, Wallet receiverWallet);
    String getName();
}
