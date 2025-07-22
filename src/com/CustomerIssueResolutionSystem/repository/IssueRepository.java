package com.CustomerIssueResolutionSystem.repository;
import com.CustomerIssueResolutionSystem.model.Issue;
import java.util.*;

public class IssueRepository {
    Map<String, Issue> issues; // {I1, issueobj}
    // for fast lookup via issueId and email we can use a Map<String, Set<String>>issuesByEmail = new ConcurrentHashMap<>()  like indexes ++ also by set duplicate issue ids are handlesd
    // Map<String, Set<String>> issuesByType = new ConcurrentHashMap<>(); // to get issues by type  // {type1, {"I1", "I2, "I3}} // In set we store issueid not issue object
    // You initilize it like issuesByType.put(t, ConcurrentHashMap.newKeySet());

    public IssueRepository(){
        this.issues = new HashMap<>(); // ++ Can use concurrent hashmap if multiple threads are used = new ConcurrentHashMap<>();
    }

    public void addIssueToRepository(Issue issue){
        issues.put(issue.getIssueId(),issue);
    }

    public Issue getIssueById (String issueId){
        if(issues.containsKey(issueId)){
            return issues.get(issueId);
        } else
            throw new IllegalArgumentException("Issue doesn't exist for id " + issueId);
    }

    public Map<String, Issue> getIssues (){
        return this.issues;
    }

}
