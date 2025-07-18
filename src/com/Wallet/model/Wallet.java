package com.Wallet.model;
import java.util.List;

public class Wallet {
    String walletId;
    Double balance;

    public Wallet(String newWalletId, double balance) {
        this.walletId = newWalletId;
        this.balance = balance;
    }
//  List<Transaction> transactionList;
    public Double getBalance(){
        return this.balance;
    }

    public void setBalance(Double balance){
        this.balance = balance;
    }
}