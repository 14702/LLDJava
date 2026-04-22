package com.MiniWallet.service.impl;

import com.MiniWallet.enums.PaymentSource;
import com.MiniWallet.enums.TransactionType;
import com.MiniWallet.exception.ErrorCode;
import com.MiniWallet.exception.WalletException;
import com.MiniWallet.model.Transaction;
import com.MiniWallet.model.Wallet;
import com.MiniWallet.service.interfaces.OfferService;
import com.MiniWallet.service.interfaces.TransactionService;
import com.MiniWallet.service.interfaces.WalletService;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

public class WalletServiceImpl implements WalletService {
    private final UserServiceImpl userService;
    private final TransactionService transactionService;
    private final OfferService offerService;

    public WalletServiceImpl(UserServiceImpl userService, TransactionService transactionService,
                             OfferService offerService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.offerService = offerService;
    }

    @Override
    public Transaction topUpWallet(String userId, double amount, PaymentSource source) {
        validateAmount(amount);
        Wallet wallet = userService.getWallet(userId);
        wallet.credit(amount);

        Transaction txn = createTransaction(userId, userId, amount, TransactionType.TOP_UP);
        transactionService.addTransaction(txn);
        return txn;
    }

    @Override
    public double fetchBalance(String userId) {
        return userService.getWallet(userId).getBalance();
    }

    @Override
    public Transaction sendMoney(String senderId, String receiverId, double amount) {
        validateAmount(amount);
        if (senderId.equals(receiverId)) {
            throw new WalletException(ErrorCode.SELF_TRANSFER);
        }

        Wallet senderWallet = userService.getWallet(senderId);
        Wallet receiverWallet = userService.getWallet(receiverId);

        Lock first = senderId.compareTo(receiverId) < 0 ? senderWallet.getLock() : receiverWallet.getLock();
        Lock second = senderId.compareTo(receiverId) < 0 ? receiverWallet.getLock() : senderWallet.getLock();

        first.lock();
        second.lock();
        try {
            if (senderWallet.getBalance() < amount) {
                throw new WalletException(ErrorCode.INSUFFICIENT_BALANCE);
            }
            senderWallet.debit(amount);
            receiverWallet.credit(amount);

            Transaction txn = createTransaction(senderId, receiverId, amount, TransactionType.SEND);

            double cashback = offerService.applyOffers(txn, senderWallet, receiverWallet);
            if (cashback > 0) {
                txn.addCashback(cashback);
                senderWallet.credit(cashback);
                Transaction cashbackTxn = createTransaction("SYSTEM", senderId, cashback, TransactionType.CASHBACK);
                transactionService.addTransaction(cashbackTxn);
            }

            transactionService.addTransaction(txn);
            return txn;
        } finally {
            second.unlock();
            first.unlock();
        }
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new WalletException(ErrorCode.INVALID_AMOUNT);
        }
    }

    private Transaction createTransaction(String senderId, String receiverId,
                                           double amount, TransactionType type) {
        return new Transaction(UUID.randomUUID().toString().substring(0, 8),
                senderId, receiverId, amount, type, LocalDateTime.now());
    }
}
