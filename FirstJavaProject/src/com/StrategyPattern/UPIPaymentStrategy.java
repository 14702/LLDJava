package com.StrategyPattern;

public class UPIPaymentStrategy implements PaymentStrategy{
    public void pay(int amount){
        System.out.println("Paid via UPI " + amount);
    }
}