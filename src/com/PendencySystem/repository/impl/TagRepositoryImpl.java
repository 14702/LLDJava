package com.PendencySystem.repository.impl;

import com.PendencySystem.exceptions.NotFoundException;
import com.PendencySystem.model.Tag;
import com.PendencySystem.repository.interfaces.TagRepository;

import java.util.List;
import java.util.Map;

public class TagRepositoryImpl implements TagRepository {

    private static volatile TagRepositoryImpl instance;
    private static Tag rootTag;

    private TagRepositoryImpl() {
        rootTag = new Tag("ROOT");
    }

    public static TagRepositoryImpl getInstance(){
        if(instance == null){
            synchronized (TagRepositoryImpl.class){
                if(instance == null){
                    instance = new TagRepositoryImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public void createTags(List<String> tags) {
        Tag currentTag = rootTag;
        for(String tagName: tags){
            Map<String, Tag> childTags = currentTag.getChildTags();
            Tag tag = null;
            if(childTags.containsKey(tagName)){
                tag = childTags.get(tagName);
            }else{
                tag = new Tag(tagName);
                childTags.put(tagName, tag);
            }
            //System.out.println("Started tracking "+ tagName);
            tag.incrementCount();
            currentTag = tag;
        }
    }

    @Override
    public void deleteTransactionFromTags(List<String> tags){
        // This method is sort of validating that hierarchy of tags exists.
        getLeafTag(tags);
        Tag currentTag = rootTag;
        for(String tagName: tags){
            Map<String, Tag> childTags = currentTag.getChildTags();
            if(!childTags.containsKey(tagName)){
                throw new NotFoundException("Tag with name " + tagName + " not found.");
            }
            //System.out.println("Stopped tracking for Tag "+ tagName);
            Tag tag = childTags.get(tagName);
            tag.decrementCount();
            currentTag = tag;
        }
    }

    @Override
    public Tag getLeafTag(List<String> tags) {
        Tag currentTag = rootTag;
        for(String tagName: tags){
            Map<String, Tag> childTags = currentTag.getChildTags();
            if(!childTags.containsKey(tagName)){
                throw new NotFoundException("Tag with name " + tagName + " not found.");
            }
            currentTag = childTags.get(tagName);
        }
        return currentTag;
    }
}