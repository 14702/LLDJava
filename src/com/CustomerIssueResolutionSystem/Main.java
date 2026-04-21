package com.CustomerIssueResolutionSystem;

import com.CustomerIssueResolutionSystem.model.IssueFilterBuilder;
import com.CustomerIssueResolutionSystem.service.interfaces.AgentService;
import com.CustomerIssueResolutionSystem.service.interfaces.IssueService;
import com.CustomerIssueResolutionSystem.service.impl.AgentServiceImpl;
import com.CustomerIssueResolutionSystem.service.impl.IssueServiceImpl;
import com.CustomerIssueResolutionSystem.exceptions.InvalidInputException;
import com.CustomerIssueResolutionSystem.repository.AgentRepository;
import com.CustomerIssueResolutionSystem.repository.IssueRepository;
import com.CustomerIssueResolutionSystem.strategy.impl.SimpleAgentAssignmentStrategy;
import com.CustomerIssueResolutionSystem.strategy.interfaces.AgentAssignmentStrategy;

import java.util.Arrays;
import java.util.Map;

public class Main {
    public static void main(String [] args){

        IssueRepository issueRepository = IssueRepository.getInstance();
        AgentRepository agentRepository = new AgentRepository();

        AgentAssignmentStrategy agentAssignmentStrategy = new SimpleAgentAssignmentStrategy();

        AgentService agentService = new AgentServiceImpl(agentRepository, issueRepository, agentAssignmentStrategy);
        IssueService issueService = new IssueServiceImpl(issueRepository, agentRepository);

        // createIssue(transactionId, issueType, subject, description, email)
        issueService.createIssue("T1", "Payment Related", "Payment Failed", "My payment failed but money is debited", "testUser1@test.com");
        issueService.createIssue("T2", "Mutual Fund Related", "Purchase Failed", "Unable to purchase Mutual Fund", "testUser2@test.com");
        issueService.createIssue("T3", "Payment Related", "Payment Failed", "My payment failed but money is debited", "testUser2@test.com");

        // addAgent(agentEmail, agentName, List<issueType>)
        agentService.addAgent("agent1@test.com", "Agent 1", Arrays.asList("Payment Related", "Gold Related"));
        agentService.addAgent("agent2@test.com", "Agent 2", Arrays.asList("Payment Related"));

        // assignIssue(issueId)
        try{
            agentService.assignIssue("I1");
            agentService.assignIssue("I2");
            agentService.assignIssue("I3");
        } catch (InvalidInputException e){
            System.out.println(e.getMessage());
        }

        // getIssues(filter) - by email
        Map<String, String> filterByEmail = new IssueFilterBuilder().byEmail("testUser2@test.com").build();
        try {
            issueService.getIssuesByFilter(filterByEmail);
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }

        // getIssues(filter) - by type
        Map<String, String> filterByType = new IssueFilterBuilder().byIssueType("Payment Related").build();
        try {
            issueService.getIssuesByFilter(filterByType);
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }

        // updateIssue(issueId, status, resolution)
        try{
            issueService.updateIssue("I3", "In Progress", "Waiting for payment confirmation");
        } catch (InvalidInputException e){
            System.out.println(e.getMessage());
        }

        // resolveIssue(issueId, resolution)
        try{
            issueService.resolveIssue("I3", "PaymentFailed debited amount will get reversed");
        } catch (InvalidInputException e){
            System.out.println(e.getMessage());
        }

        // viewAgentsWorkHistory()
        agentService.viewAgentsWorkHistory();
    }
}
