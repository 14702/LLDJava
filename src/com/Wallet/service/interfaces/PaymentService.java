package com.Wallet.service.interfaces;

import java.util.List;
import com.Wallet.FilterStrategy.interfaces.TransactionFilterStrategy;
import com.Wallet.SortStrategy.interfaces.TransactionSortStrategy;
import com.Wallet.enums.Ordering;
import com.Wallet.enums.PaymentMode;
import com.Wallet.model.Transaction;

public interface PaymentService {
    Transaction payment(String payer, String payee, Double amount, PaymentMode paymentMode);

    Transaction topUpWallet(String userId, Double amount, PaymentMode paymentMode);

    List<Transaction> getTransaction(String userId, TransactionSortStrategy transactionSortStrategy, Ordering ordering,
                                     List<TransactionFilterStrategy> transactionFilterStrategies);


}