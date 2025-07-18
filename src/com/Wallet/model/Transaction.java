package com.Wallet.model;
import java.sql.Timestamp;
import java.util.UUID;

import com.Wallet.enums.PaymentMode;
import com.Wallet.enums.TransactionType;

public class Transaction {
    String transactionId;
    String payer;
    String payee;
    double amount;
    TransactionType transactionType;
    Timestamp timestamp;
    PaymentMode paymentMode;

    public Transaction(String transactionId, String payer, String payee, Double amount, TransactionType transactionType, Timestamp timestamp, PaymentMode paymentMode) {
        this.transactionId = transactionId;
        this.payer = payer;
        this.payee = payee;
        this.amount = amount;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
        this.paymentMode = paymentMode;
    }

    public TransactionType getTransactionType(){
        return this.transactionType;
    }

    public Timestamp getTimestamp(){
        return this.timestamp;
    }

    public String getPayee(){
        return this.payee;
    }

    public String getPayer(){
        return this.payer;
    }

    public double getAmount(){
        return this.amount;
    }

}