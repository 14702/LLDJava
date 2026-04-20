package com.CustomerIssueResolutionSystem.model;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Agent {
    // ✅ FIX: Use AtomicInteger for thread-safe counter
    private static final AtomicInteger agentCounter = new AtomicInteger(1);
    
    String agentId;
    String email;
    String name;
    List<String> expertiseList;
    Issue currIssue;
    List<String> workingHistory;
    Queue<Issue> waitingIssuesList;

    public Agent(String email, String name, List<String> expertiseList){
        // ✅ FIX: Add validation
        if(email == null || email.trim().isEmpty())
            throw new IllegalArgumentException("Email cannot be null or empty");
        if(name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty");
        if(expertiseList == null || expertiseList.isEmpty())
            throw new IllegalArgumentException("Expertise list cannot be null or empty");
            
        this.agentId = "A" + agentCounter.getAndIncrement();  // ✅ Thread-safe
        this.email = email.trim();
        this.name = name.trim();
        this.expertiseList = new ArrayList<>(expertiseList);  // ✅ Defensive copy
        this.currIssue = null;
        this.workingHistory = new ArrayList<>();
        this.waitingIssuesList = new LinkedList<>();
    }

    public List<String> getWorkingHistory(){
        return new ArrayList<>(this.workingHistory);  // ✅ Return copy to prevent external modification
    }
    public void addToWorkingHistory(String issueId){
        this.workingHistory.add(issueId);
    }
    public List<String> getExpertiseList(){
        return new ArrayList<>(this.expertiseList);  // ✅ Return copy
    }
    public Issue getCurrIssue(){
        return this.currIssue;
    }
    public void setCurrIssue(Issue currIssue){
        this.currIssue = currIssue;
    }
    public String getAgentId(){
        return this.agentId;
    }
    public void addToWaitingIssuesList(Issue issue){
        this.waitingIssuesList.add(issue);
    }
    public Queue<Issue> getWaitingIssuesList(){
        return this.waitingIssuesList;
    }
    public String getEmail() {
        return this.email;
    }
}
