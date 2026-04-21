package com.CustomerIssueResolutionSystem.model;

import java.util.HashMap;
import java.util.Map;

public class IssueFilterBuilder {
    Map<String, String> filter;

    public IssueFilterBuilder(){
        this.filter = new HashMap<>();
    }

    public IssueFilterBuilder byEmail(String email){
        this.filter.put("email", email);
        return this;
    }

    public IssueFilterBuilder byIssueType(String issueType){
        this.filter.put("issueType", issueType);
        return this;
    }

    public Map<String, String> build(){
        return filter;
    }
}
