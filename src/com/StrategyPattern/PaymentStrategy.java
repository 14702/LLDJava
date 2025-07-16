package com.StrategyPattern;

public interface PaymentStrategy {
    void pay(int amount);
}

// use abstract to explicitly tell abstract method