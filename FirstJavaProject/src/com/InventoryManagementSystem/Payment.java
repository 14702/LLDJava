package com.InventoryManagementSystem;

public class Payment {
    int amount;

    public static boolean makePayment(int amount){
        System.out.println("Payment succeeded for amount "+ amount);
        return true;
    }

}
