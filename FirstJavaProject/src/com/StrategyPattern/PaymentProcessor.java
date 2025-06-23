package com.StrategyPattern;

public class PaymentProcessor {
    PaymentStrategy strategy;

    public void setPaymentStrategy(PaymentStrategy strategy){
        this.strategy = strategy;
    }

    public void process(int amount){
        strategy.pay(amount);
    }
}
