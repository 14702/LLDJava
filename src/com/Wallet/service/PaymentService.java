package com.Wallet.service;

import java.util.List;
import com.Wallet.FilterStrategy.TransactionFilterStrategy;
import com.Wallet.SortStrategy.TransactionSortStrategy;
import com.Wallet.enums.Ordering;
import com.Wallet.enums.PaymentMode;
import com.Wallet.model.Transaction;

public interface PaymentService {
    Transaction payment(String payer, String payee, Double amount, PaymentMode paymentMode);

    Transaction topUpWallet(String userId, Double amount, PaymentMode paymentMode);

    List<Transaction> getTransaction(String userId, TransactionSortStrategy transactionSortStrategy, Ordering ordering,
                                     List<TransactionFilterStrategy> transactionFilterStrategies);


}