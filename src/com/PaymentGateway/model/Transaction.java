package com.PaymentGateway.model;

import com.PaymentGateway.enums.PaymentMethod;
import com.PaymentGateway.enums.TransactionStatus;
import java.util.UUID;

public class Transaction {
    private final String transactionId;
    private final String clientId;
    private final String bankName;
    private final PaymentMethod method;
    private final double amount;
    private final TransactionStatus status;

    public Transaction(String clientId, String bankName, PaymentMethod method,
                       double amount, TransactionStatus status) {
        this.transactionId = UUID.randomUUID().toString().substring(0, 8);
        this.clientId = clientId;
        this.bankName = bankName;
        this.method = method;
        this.amount = amount;
        this.status = status;
    }

    public String getTransactionId() { return transactionId; }
    public String getClientId() { return clientId; }
    public String getBankName() { return bankName; }
    public PaymentMethod getMethod() { return method; }
    public double getAmount() { return amount; }
    public TransactionStatus getStatus() { return status; }

    @Override
    public String toString() {
        return "Txn[" + transactionId + "] client=" + clientId + " bank=" + bankName +
               " method=" + method + " amount=" + amount + " status=" + status;
    }
}
