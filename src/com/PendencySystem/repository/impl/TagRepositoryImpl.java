package com.PendencySystem.repository.impl;

import com.PendencySystem.model.Tag;
import com.PendencySystem.repository.interfaces.TagRepository;

import java.util.List;

public class TagRepositoryImpl implements TagRepository {

    private final Tag rootTag = new Tag("ROOT");

    @Override
    public void incrementTags(List<String> tags) {
        Tag current = rootTag;
        for (String tagName : tags) {
            current = current.getOrCreateChild(tagName);
            current.incrementCount();
        }
    }

    @Override
    public void decrementTags(List<String> tags) {
        Tag current = rootTag;
        for (String tagName : tags) {
            Tag child = current.getChild(tagName);
            if (child == null) return;
            child.decrementCount();
            current = child;
        }
    }

    @Override
    public int getCounts(List<String> tags) {
        Tag current = rootTag;
        for (String tagName : tags) {
            Tag child = current.getChild(tagName);
            if (child == null) return 0;
            current = child;
        }
        return current.getCount();
    }
}
