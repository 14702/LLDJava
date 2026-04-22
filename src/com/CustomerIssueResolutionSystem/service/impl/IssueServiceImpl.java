package com.CustomerIssueResolutionSystem.service.impl;

import com.CustomerIssueResolutionSystem.enums.IssueStatus;
import com.CustomerIssueResolutionSystem.enums.IssueType;
import com.CustomerIssueResolutionSystem.exceptions.InvalidInputException;
import com.CustomerIssueResolutionSystem.exceptions.IssueNotFoundException;
import com.CustomerIssueResolutionSystem.repository.interfaces.AgentRepositoryInterface;
import com.CustomerIssueResolutionSystem.repository.interfaces.IssueRepositoryInterface;
import com.CustomerIssueResolutionSystem.model.Issue;
import com.CustomerIssueResolutionSystem.model.Agent;
import com.CustomerIssueResolutionSystem.service.interfaces.IssueService;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;  // ✅ Now used

public class IssueServiceImpl implements IssueService {
    IssueRepositoryInterface issueRepository;
    AgentRepositoryInterface agentRepository;

    public IssueServiceImpl(IssueRepositoryInterface issueRepository, AgentRepositoryInterface agentRepository){
        this.issueRepository = issueRepository;
        this.agentRepository = agentRepository;
    }
    
    @Override
    public Issue createIssue(String transId, String issueType, String subject, 
                            String description, String customerEmail){
        // ✅ FIX: Move all validation to Issue constructor
        // Issue constructor now handles all validation
        try {
            Issue issue = new Issue(transId, IssueType.getIssueType(issueType), 
                                   subject, description, customerEmail);
            issueRepository.addIssueToRepository(issue);
            System.out.println("Issue " + issue.getIssueId() + " created against Transaction "+ transId);
            return issue;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to create issue: " + e.getMessage());
        }
    }

    @Override
    public void getIssues(IssueType issueType){
        if(issueType == null) throw new IllegalArgumentException("Issue type cannot be null");
        
        System.out.println("Getting Issue filtered by Type: " + issueType);
        List<Issue> filtered = issueRepository.getIssues().values()
                .stream()
                .filter(issue -> issue.getIssueType().equals(issueType))
                .collect(Collectors.toList());
        
        if(filtered.isEmpty()) {
            System.out.println("No issues found for type: " + issueType);
        } else {
            filtered.forEach(this::printIssue);
        }
    }

    @Override
    public void getIssues(String email) throws InvalidInputException{
        if(email == null || email.trim().isEmpty())
            throw new InvalidInputException("Non empty email must be provided to filter via email");

        System.out.println("Getting Issue filtered by mail: " + email);
        
        List<Issue> filtered = issueRepository.getIssues().values()
                .stream()
                .filter(issue -> issue.getCustomerEmail().equals(email.trim()))
                .collect(Collectors.toList());
        
        if(filtered.isEmpty()) {
            System.out.println("No issues found for customer: " + email);
        } else {
            filtered.forEach(this::printIssue);
        }
    }

    // ✅ NEW: Implement builder-based filtering
    public void getIssuesByFilter(Map<String, String> filter) throws InvalidInputException {
        if(filter == null || filter.isEmpty())
            throw new InvalidInputException("Filter map cannot be empty");
        
        List<Issue> filtered = issueRepository.getIssues().values()
                .stream()
                .filter(issue -> {
                    if(filter.containsKey("email") && 
                       !issue.getCustomerEmail().equals(filter.get("email")))
                        return false;
                    if(filter.containsKey("issueType")) {
                        try {
                            IssueType type = IssueType.getIssueType(filter.get("issueType"));
                            if(!issue.getIssueType().equals(type))
                                return false;
                        } catch (IllegalArgumentException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if(filter.containsKey("status")) {
                        IssueStatus status = IssueStatus.getIssueStatus(filter.get("status"));
                        if(!issue.getIssueStatus().equals(status))
                            return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
        
        if(filtered.isEmpty()) {
            System.out.println("No issues found matching filter: " + filter);
        } else {
            System.out.println("Found " + filtered.size() + " issues:");
            filtered.forEach(this::printIssue);
        }
    }

    @Override
    public void printIssue (Issue issue){                                                                               // ++ updated printissue
        //I2 {"T2", "Mutual Fund Related", "Purchase Failed", "Unable to purchase Mutual Fund", "testUser2@test.com", "Open"},
            System.out.printf("%s { %s , %s , %s , %s , %s , %s }%n",
                    issue.getIssueId(),
                    issue.getTransId(),
                    IssueType.getIssueDescription(issue.getIssueType()),
                    issue.getSubject(),
                    issue.getDescription(),
                    issue.getCustomerEmail(),
                    issue.getIssueStatus());
    }

    @Override
    public void updateIssue(String issueId, String issueStatus, String description) 
            throws InvalidInputException{
        if(issueId == null || issueId.trim().isEmpty())
            throw new InvalidInputException("Issue ID cannot be null or empty");
        if(issueStatus == null || issueStatus.trim().isEmpty())
            throw new InvalidInputException("Issue status cannot be null or empty");
        if(description == null || description.trim().isEmpty())
            throw new InvalidInputException("Description cannot be null or empty");
            
        Issue issue = issueRepository.getIssueById(issueId);
        if(issue == null)
            throw new InvalidInputException("Please create the issue before updating it");
        
        // ✅ FIX: All state changes in synchronized block
        synchronized (issue){
            issue.setDescription(description.trim());
            issue.setIssueStatus(IssueStatus.getIssueStatus(issueStatus));
            System.out.println(issueId + " status updated to "+ issueStatus);
        }
    }

    @Override
    public void resolveIssue(String issueId, String description) throws InvalidInputException{
        if(issueId == null || issueId.trim().isEmpty())
            throw new InvalidInputException("Issue ID cannot be null or empty");
        if(description == null || description.trim().isEmpty())
            throw new InvalidInputException("Resolution description cannot be null or empty");
            
        Issue issue = issueRepository.getIssueById(issueId);
        if(issue == null)
            throw new IssueNotFoundException("Please create the issue before resolving it");

        // ✅ FIX: Better synchronization and logic
        synchronized (issue){
            // ✅ FIX: Cannot resolve already resolved issues
            if(issue.getIssueStatus() == IssueStatus.RESOLVED) {
                System.out.println("Issue " + issueId + " is already resolved");
                return;
            }
            
            issue.setResolution(description.trim());  // ✅ FIX: Set resolution separately
            issue.setIssueStatus(IssueStatus.RESOLVED);
            System.out.println(issueId + " issue marked resolved");

            // ✅ FIX: Free the agent - handle null cases
            if(issue.getAgentID() != null && !issue.getAgentID().isEmpty()){
                try {
                    Agent agent = agentRepository.getAgentById(issue.getAgentID());
                    Queue<Issue> waitingList = agent.getWaitingIssuesList();
                    
                    if(waitingList.isEmpty()){
                        agent.setCurrIssue(null);
                        System.out.println("Agent " + agent.getAgentId() + " is now free");
                    } else {
                        Issue nextIssue = waitingList.poll();
                        agent.setCurrIssue(nextIssue);
                        nextIssue.setIssueStatus(IssueStatus.ASSIGNED);
                        agent.addToWorkingHistory(nextIssue.getIssueId());
                        System.out.println("Agent " + agent.getAgentId() + " assigned next issue from waiting list");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Agent not found for issue: " + e.getMessage());
                }
            }
        }
    }
}
