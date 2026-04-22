package com.PendencySystem.repository.interfaces;

import com.PendencySystem.model.Transaction;

import java.util.List;

public interface TransactionRepository {
    void create(Integer transactionId, List<String> allTags);
    Transaction get(Integer id);
    Transaction remove(Integer id);
}
