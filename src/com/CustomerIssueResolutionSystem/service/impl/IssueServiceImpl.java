package com.CustomerIssueResolutionSystem.service.impl;

import com.CustomerIssueResolutionSystem.enums.IssueStatus;
import com.CustomerIssueResolutionSystem.enums.IssueType;
import com.CustomerIssueResolutionSystem.exceptions.InvalidInputException;
import com.CustomerIssueResolutionSystem.repository.AgentRepository;
import com.CustomerIssueResolutionSystem.repository.IssueRepository;
import com.CustomerIssueResolutionSystem.model.Issue;
import com.CustomerIssueResolutionSystem.model.Agent;
import com.CustomerIssueResolutionSystem.service.interfaces.IssueService;

public class IssueServiceImpl implements IssueService {
    IssueRepository issueRepository;
    AgentRepository agentRepository;

    public IssueServiceImpl(IssueRepository issueRepository, AgentRepository agentRepository){
        this.issueRepository = issueRepository;
        this.agentRepository = agentRepository;
    }
    
    // "T3", "Payment Related", "Payment Failed", "My payment failed but money is debited",“testUser2@test.com”
    @Override
    public Issue createIssue(String transId, String issueType, String subject, String description, String customerEmail){
        Issue issue = new Issue(transId, IssueType.getIssueType(issueType), subject, description, customerEmail);
        issueRepository.addIssueToRepository(issue);
        System.out.println("Issue " + issue.getIssueId() + " created against Transaction "+ transId);
        return issue;
    }

    @Override
    public void getIssuesByEmail(String email) throws InvalidInputException{
        if(email.isEmpty())
            throw new InvalidInputException("Non empty email must be provided to filter via email");

        System.out.println("Getting Issue filtered by mail: " + email);

        for(Issue issue : issueRepository.getIssues().values()){
            if(issue.getCustomerEmail().equals(email)){
                printIssue(issue);
            }
        }
    }
    //getIssue({"type": "Payment Related"});
    @Override
    public void getIssuesByType(IssueType issueType){
        // Issue type already checked in ENUM class
        System.out.println("Getting Issue filtered by Type: " + issueType);

        for(Issue issue : issueRepository.getIssues().values()){
            if(issue.getIssueType() == issueType){
                printIssue(issue);
            }
        }
    }

    @Override
    public void printIssue (Issue issue){
        //I2 {"T2", "Mutual Fund Related", "Purchase Failed", "Unable to purchase Mutual Fund", "testUser2@test.com", "Open"},
            System.out.println(issue.getIssueId() + "{ " +  issue.getTransId() + "," + IssueType.getIssueDescription(issue.getIssueType()) + " , " + issue.getSubject() + " , "+ issue.getDescription() + " , " + issue.getCustomerEmail() + " , " + issue.getIssueStatus());
    }

    @Override
    public void updateIssue(String issueId, String issueStatus, String description) throws InvalidInputException{
        Issue issue = issueRepository.getIssueById(issueId);
        if(issue == null)
            throw new InvalidInputException("Please create the issue before updating it");
        issue.setDescription(description);
        issue.setIssueStatus(IssueStatus.getIssueStatus(issueStatus));
        System.out.println(issueId + " status updated to "+ issueStatus);
    }

    @Override
    public void resolveIssue(String issueId, String description) throws InvalidInputException{
        Issue issue = issueRepository.getIssueById(issueId);

        if(issue == null)
            throw new InvalidInputException("Please create the issue before resolving it");

        issue.setDescription(description);
        issue.setIssueStatus(IssueStatus.RESOLVED);
        System.out.println(issueId + " issue marked resolved");

        // Free the agent
        Agent agent = agentRepository.getAgentById(issue.getAgentID());
        agent.setCurrIssue(null);
        // add the issue to agent working history
        agent.addToWorkingHistory(issueId);
    }
}
