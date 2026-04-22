package com.PaymentGateway.model;

import com.PaymentGateway.enums.TransactionStatus;
import java.util.Random;

public class Bank {
    private final String name;
    private final Random random = new Random();
    private int totalRequests;
    private int successCount;

    public Bank(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public synchronized TransactionStatus processPayment(PaymentDetails details) {
        totalRequests++;
        boolean success = random.nextDouble() > 0.2; // 80% success rate
        if (success) successCount++;
        return success ? TransactionStatus.SUCCESS : TransactionStatus.FAILURE;
    }

    public synchronized double getSuccessRate() {
        return totalRequests == 0 ? 1.0 : (double) successCount / totalRequests;
    }

    public synchronized int getTotalRequests() { return totalRequests; }

    @Override
    public String toString() { return "Bank[" + name + "]"; }
}
