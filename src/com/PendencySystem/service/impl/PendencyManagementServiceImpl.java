package com.PendencySystem.service.impl;

import com.PendencySystem.exceptions.NotFoundException;
import com.PendencySystem.model.Tag;
import com.PendencySystem.model.Transaction;
import com.PendencySystem.repository.interfaces.TagRepository;
import com.PendencySystem.repository.interfaces.TransactionRepository;
import com.PendencySystem.service.interfaces.PendencyManagementService;

import java.util.List;

public class PendencyManagementServiceImpl implements PendencyManagementService {

    private final TransactionRepository transactionRepository;
    private final TagRepository tagRepository;

    public PendencyManagementServiceImpl(TransactionRepository transactionRepository, TagRepository tagRepository){
        this.tagRepository = tagRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void startTracking(Integer id, List<String> hierarchicalTags) {
        transactionRepository.create(id, hierarchicalTags);
        tagRepository.createTags(hierarchicalTags);
    }

    @Override
    public void stopTracking(Integer id) {
        Transaction transaction = transactionRepository.get(id);
        List<String> hierarchicalTags = transaction.getTags();
        tagRepository.deleteTransactionFromTags(hierarchicalTags);
        transactionRepository.remove(id);

    }

    @Override
    public Integer getCounts(List<String> tags) {
        try{
            Tag leafTag = tagRepository.getLeafTag(tags);
            return leafTag.getCount();
        }catch (NotFoundException exception){
            return 0;
        }
    }
}