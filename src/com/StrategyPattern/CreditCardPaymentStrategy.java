package com.StrategyPattern;

public class CreditCardPaymentStrategy implements PaymentStrategy{
    public void pay (int amount){
        System.out.println("Paid via credit card " + amount);
    }
}
