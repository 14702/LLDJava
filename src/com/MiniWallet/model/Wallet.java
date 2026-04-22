package com.MiniWallet.model;

import java.util.concurrent.locks.ReentrantLock;

public class Wallet {
    private final String userId;
    private double balance;
    private final ReentrantLock lock = new ReentrantLock();

    public Wallet(String userId) {
        this.userId = userId;
        this.balance = 0;
    }

    public String getUserId() { return userId; }

    public double getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public void credit(double amount) {
        lock.lock();
        try {
            balance += amount;
        } finally {
            lock.unlock();
        }
    }

    public void debit(double amount) {
        lock.lock();
        try {
            balance -= amount;
        } finally {
            lock.unlock();
        }
    }

    public ReentrantLock getLock() { return lock; }
}
