package com.Wallet.model;

import java.util.concurrent.locks.ReentrantLock;

public class Wallet {
    private final String walletId;
    private double balance;
    private final ReentrantLock lock = new ReentrantLock();

    public Wallet(String walletId, double balance) {
        this.walletId = walletId;
        this.balance = balance;
    }

    public double getBalance() {
        lock.lock();
        try { return this.balance; } finally { lock.unlock(); }
    }

    public void setBalance(double balance) {
        lock.lock();
        try { this.balance = balance; } finally { lock.unlock(); }
    }

    public String getWalletId() { return walletId; }
    public ReentrantLock getLock() { return lock; }
}
