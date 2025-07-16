package com.CustomerIssueResolutionSystem.repository;
import com.CustomerIssueResolutionSystem.model.Issue;
import java.util.*;

public class IssueRepository {
    Map<String, Issue> issues;

    public IssueRepository(){
        this.issues = new HashMap<>();
    }

    public void addIssueToRepo(Issue issue){
        issues.put(issue.getIssueId(),issue);
    }

    public Issue getIssueById (String issueId){
        if(issues.containsKey(issueId)){
            return issues.get(issueId);
        } else throw new IllegalArgumentException("Issue doesn't exist for id " + issueId);
    }

    public Map<String, Issue> getIssues (){
        return this.issues;
    }

}
