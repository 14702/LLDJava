package com.PendencySystem.model;

import java.util.Collections;
import java.util.List;

public class Transaction {
    private final Integer id;
    private final List<String> tags;

    public Transaction(Integer id, List<String> tags) {
        this.id = id;
        this.tags = Collections.unmodifiableList(tags);
    }

    public Integer getId() { return id; }
    public List<String> getTags() { return tags; }
}
