package com.Wallet.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.Wallet.model.Transaction;

public class PaymentData {
    Map<String, List<Transaction>> userIdToTransactionList = new HashMap<>();

    public Map<String, List<Transaction>> getUserIdToTransactionList(){
        return this.userIdToTransactionList;
    }
}