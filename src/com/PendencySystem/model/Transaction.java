package com.PendencySystem.model;

//import lombok.Getter;

import java.util.List;

//@Getter
public class Transaction {
    private final Integer id;
    private final List<String> tags;

    public Transaction(Integer id, List<String> tags) {
        this.id = id;
        this.tags = tags;
    }

    public Integer getId() {
        return id;
    }

    public List<String> getTags(){
        return this.tags;
    }
}