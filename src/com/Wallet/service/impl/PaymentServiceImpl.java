package com.Wallet.service.impl;

import static java.time.Instant.now;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import com.Wallet.FilterStrategy.interfaces.TransactionFilterStrategy;
import com.Wallet.SortStrategy.interfaces.TransactionSortStrategy;
import com.Wallet.repository.PaymentData;
import com.Wallet.enums.Ordering;
import com.Wallet.enums.PaymentMode;
import com.Wallet.enums.TransactionType;
import com.Wallet.model.Transaction;
import com.Wallet.model.Wallet;
import com.Wallet.service.interfaces.PaymentService;
import com.Wallet.service.interfaces.WalletServiceInternal;

public class PaymentServiceImpl implements PaymentService {

    private WalletServiceInternal walletService;
    private PaymentData paymentData;

    public  PaymentServiceImpl(PaymentData paymentData, WalletServiceInternal walletService){
        this.paymentData = paymentData;
        this.walletService = walletService;
    }

    @Override
    public Transaction payment(String payer, String payee, Double amount, PaymentMode paymentMode) {
        Wallet payerWallet = walletService.getWalletByUserId(payer);
        Wallet payeeWallet = walletService.getWalletByUserId(payee);

        Double payerUpdatedBalance = payerWallet.getBalance() - amount;
        Double payeeUpdatedBalance = payeeWallet.getBalance() + amount;

        double cashBack = 0;
        if(payerUpdatedBalance.doubleValue() ==  payeeUpdatedBalance.doubleValue()){
            cashBack = (amount * 5)/100;
        }
        if(payerUpdatedBalance < 0) throw new RuntimeException("Insufficient Balance");
        payerWallet.setBalance(payerUpdatedBalance + cashBack);
        payeeWallet.setBalance(payeeUpdatedBalance + cashBack);


        Transaction debit = new Transaction(UUID.randomUUID().toString(), payer, payee, amount, TransactionType.DEBIT,
                new Timestamp(System.currentTimeMillis()), paymentMode);
        appendTransactionToUser(payer, debit);

        Transaction credit = new Transaction(UUID.randomUUID().toString(), payer, payee, amount, TransactionType.CREDIT,
                new Timestamp(System.currentTimeMillis()), paymentMode);
        appendTransactionToUser(payee, credit);

        if(cashBack != 0){

            Transaction creditCashbackToPayer = new Transaction(UUID.randomUUID().toString(), "CashBack", payer, cashBack, TransactionType.CREDIT,
                    new Timestamp(System.currentTimeMillis()), PaymentMode.CASH_BACK);
            appendTransactionToUser(payer, creditCashbackToPayer);

            Transaction  creditCashbackToPayee = new Transaction(UUID.randomUUID().toString(), "CashBack", payee, cashBack, TransactionType.CREDIT,
                    new Timestamp(System.currentTimeMillis()), PaymentMode.CASH_BACK);
            appendTransactionToUser(payee, creditCashbackToPayee);
        }

        return debit;
    }

    private void appendTransactionToUser(String payer, Transaction debit) {
        List<Transaction> transactionList = paymentData.getUserIdToTransactionList().getOrDefault(payer, new ArrayList<>());
        transactionList.add(debit);
        paymentData.getUserIdToTransactionList().put(payer, transactionList);
    }

    @Override
    public Transaction topUpWallet(String userId, Double amount, PaymentMode paymentMode) {
        Wallet wallet = walletService.getWalletByUserId(userId);
        double cashBack = 0;
        if(amount >= 100 && paymentData.getUserIdToTransactionList().getOrDefault(userId, new ArrayList<>()).isEmpty()){
            cashBack = (amount * 10)/100;
        }
        wallet.setBalance(wallet.getBalance() + amount +cashBack);

        Transaction transaction = new Transaction(UUID.randomUUID().toString(), userId, userId, amount, TransactionType.TOP_UP,
                new Timestamp(System.currentTimeMillis()), paymentMode);
        appendTransactionToUser(userId, transaction);

        if(cashBack != 0){
            Transaction cashBackTransaction = new Transaction(UUID.randomUUID().toString(), "CashBack", userId, cashBack, TransactionType.CREDIT,
                    new Timestamp(System.currentTimeMillis()), PaymentMode.CASH_BACK);
            appendTransactionToUser(userId, cashBackTransaction);
        }

        return transaction;
    }

    @Override
    public List<Transaction> getTransaction(String userId,
                                            TransactionSortStrategy transactionSortStrategy, Ordering ordering,
                                            List<TransactionFilterStrategy> transactionFilterStrategies) {

        List<Transaction> transactionList = paymentData.getUserIdToTransactionList().getOrDefault(userId, new ArrayList<>());

        if(Objects.nonNull(transactionSortStrategy))  transactionSortStrategy.sort(transactionList, ordering);
        if(Objects.nonNull(transactionFilterStrategies)){
            for(TransactionFilterStrategy filterStrategy: transactionFilterStrategies){
                transactionList = filterStrategy.filter(transactionList);
            }
        }

        System.out.println("Transaction Records of User " + userId);
        System.out.println(transactionList);

        return transactionList;
    }
}