package com.MiniWallet.model;

import com.MiniWallet.enums.TransactionType;

import java.time.LocalDateTime;

public class Transaction {
    private final String transactionId;
    private final String senderId;
    private final String receiverId;
    private final double amount;
    private final TransactionType type;
    private final LocalDateTime timestamp;
    private double cashback;

    public Transaction(String transactionId, String senderId, String receiverId,
                       double amount, TransactionType type, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
        this.cashback = 0;
    }

    public String getTransactionId() { return transactionId; }
    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public double getAmount() { return amount; }
    public TransactionType getType() { return type; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public double getCashback() { return cashback; }

    public void addCashback(double cashback) {
        this.cashback += cashback;
    }

    @Override
    public String toString() {
        String cb = cashback > 0 ? ", cashback=" + String.format("%.2f", cashback) : "";
        return "Txn{" + transactionId + ", " + type + ", " + senderId + " -> " + receiverId +
                ", amt=" + String.format("%.2f", amount) + cb + ", " + timestamp.toLocalDate() + "}";
    }
}
