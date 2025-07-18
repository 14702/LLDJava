package com.PendencySystem.repository.interfaces;

import com.PendencySystem.model.Tag;

import java.util.List;

public interface TagRepository {

    void createTags(List<String> tags);
    void deleteTransactionFromTags(List<String> tags);
    Tag getLeafTag(List<String> tags);
}