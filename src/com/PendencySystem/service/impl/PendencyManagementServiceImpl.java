package com.PendencySystem.service.impl;

import com.PendencySystem.model.Transaction;
import com.PendencySystem.repository.interfaces.TagRepository;
import com.PendencySystem.repository.interfaces.TransactionRepository;
import com.PendencySystem.service.interfaces.PendencyManagementService;

import java.util.List;

public class PendencyManagementServiceImpl implements PendencyManagementService {

    private final TransactionRepository transactionRepository;
    private final TagRepository tagRepository;

    public PendencyManagementServiceImpl(TransactionRepository transactionRepository,
                                         TagRepository tagRepository) {
        this.transactionRepository = transactionRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public void startTracking(Integer id, List<String> hierarchicalTags) {
        if (id == null || hierarchicalTags == null || hierarchicalTags.isEmpty()) {
            throw new IllegalArgumentException("ID and tags are required");
        }
        transactionRepository.create(id, hierarchicalTags);
        tagRepository.incrementTags(hierarchicalTags);
    }

    @Override
    public void stopTracking(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID is required");
        }
        Transaction transaction = transactionRepository.get(id);
        tagRepository.decrementTags(transaction.getTags());
        transactionRepository.remove(id);
    }

    @Override
    public int getCounts(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            throw new IllegalArgumentException("Tags are required");
        }
        return tagRepository.getCounts(tags);
    }
}
