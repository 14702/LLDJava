package com.MiniWallet.service.interfaces;

import com.MiniWallet.enums.PaymentSource;
import com.MiniWallet.model.Transaction;

public interface WalletService {
    Transaction topUpWallet(String userId, double amount, PaymentSource source);
    double fetchBalance(String userId);
    Transaction sendMoney(String senderId, String receiverId, double amount);
}
