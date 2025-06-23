package com.StrategyPattern;

public class Main {
    public static void main (String [] args){
        PaymentProcessor paymentProcessor = new PaymentProcessor();     // Processor is just a container for strategy VAR and strategy METHOD

        PaymentStrategy strategy = new UPIPaymentStrategy();            // Set the strategy and pay
        paymentProcessor.setPaymentStrategy(strategy);
        paymentProcessor.process(2999);

        strategy = new CreditCardPaymentStrategy();
        paymentProcessor.setPaymentStrategy(strategy);
        paymentProcessor.process(13999);
    }
}
