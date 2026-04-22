package com.Wallet.repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.Wallet.model.Transaction;

public class PaymentData {
    Map<String, List<Transaction>> userIdToTransactionList = new ConcurrentHashMap<>();

    public Map<String, List<Transaction>> getUserIdToTransactionList(){
        return this.userIdToTransactionList;
    }
}