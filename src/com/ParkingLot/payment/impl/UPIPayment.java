package com.ParkingLot.payment.impl;

import com.ParkingLot.payment.interfaces.PaymentMethod;

public class UPIPayment implements PaymentMethod {
    @Override
    public boolean pay(int amount) {
        System.out.println("Payment received via UPI: " + amount);
        return true;
    }
}
