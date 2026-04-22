package com.PendencySystem.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Tag {
    private final String name;
    private final ConcurrentHashMap<String, Tag> childTags = new ConcurrentHashMap<>();
    private final AtomicInteger count = new AtomicInteger(0);

    public Tag(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public ConcurrentHashMap<String, Tag> getChildTags() { return childTags; }
    public int getCount() { return count.get(); }
    public int incrementCount() { return count.incrementAndGet(); }
    public int decrementCount() { return count.decrementAndGet(); }

    public Tag getOrCreateChild(String childName) {
        return childTags.computeIfAbsent(childName, Tag::new);
    }

    public Tag getChild(String childName) {
        return childTags.get(childName);
    }
}
