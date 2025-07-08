package com.ParkingLot;

public class UPIPayment extends Payment{
    @Override
    public boolean payTicket(int amount){
        System.out.println("Payment received via UPI for amount : "+ amount);
        return true;
    }
}
