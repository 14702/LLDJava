package com.CustomerIssueResolutionSystem.repository.impl;
import com.CustomerIssueResolutionSystem.enums.IssueType;
import com.CustomerIssueResolutionSystem.model.Issue;
import com.CustomerIssueResolutionSystem.repository.interfaces.IssueRepositoryInterface;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class IssueRepository implements IssueRepositoryInterface {
    private static IssueRepository instance;
    Map<String, Issue> issues;
    private final Map<String, Set<String>> issuesByEmail = new ConcurrentHashMap<>();
    private final Map<IssueType, Set<String>> issuesByType = new ConcurrentHashMap<>();

    private IssueRepository(){
        this.issues = new ConcurrentHashMap<>();
    }

    public static synchronized IssueRepository getInstance(){
        if(instance == null){
            instance = new IssueRepository();
        }
        return instance;
    }

    @Override
    public void addIssueToRepository(Issue issue){
        if(issue == null) throw new IllegalArgumentException("Issue cannot be null");

        issues.put(issue.getIssueId(), issue);

        issuesByEmail.computeIfAbsent(issue.getCustomerEmail(),
                k -> ConcurrentHashMap.newKeySet()).add(issue.getIssueId());
        issuesByType.computeIfAbsent(issue.getIssueType(),
                k -> ConcurrentHashMap.newKeySet()).add(issue.getIssueId());
    }

    @Override
    public Issue getIssueById(String issueId){
        if(issueId == null || issueId.isEmpty())
            throw new IllegalArgumentException("Issue ID cannot be null or empty");
        if(!issues.containsKey(issueId))
            throw new IllegalArgumentException("Issue doesn't exist for id " + issueId);
        return issues.get(issueId);
    }

    @Override
    public Map<String, Issue> getIssues(){
        return new ConcurrentHashMap<>(this.issues);
    }

    @Override
    public void removeIssueFromRepository(String issueId) {
        if(issueId == null || issueId.isEmpty())
            throw new IllegalArgumentException("Issue ID cannot be null");

        Issue issue = issues.remove(issueId);
        if(issue != null) {
            issuesByEmail.get(issue.getCustomerEmail()).remove(issueId);
            issuesByType.get(issue.getIssueType()).remove(issueId);
        }
    }
}
