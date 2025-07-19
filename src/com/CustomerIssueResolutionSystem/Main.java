package com.CustomerIssueResolutionSystem;

import com.CustomerIssueResolutionSystem.service.interfaces.AgentService;
import com.CustomerIssueResolutionSystem.service.interfaces.IssueService;
import com.CustomerIssueResolutionSystem.service.impl.AgentServiceImpl;
import com.CustomerIssueResolutionSystem.service.impl.IssueServiceImpl;
import com.CustomerIssueResolutionSystem.model.Issue;
import com.CustomerIssueResolutionSystem.enums.IssueType;
import com.CustomerIssueResolutionSystem.exceptions.InvalidInputException;
import com.CustomerIssueResolutionSystem.repository.AgentRepository;
import com.CustomerIssueResolutionSystem.repository.IssueRepository;
import java.util.Arrays;

public class Main {
    public static void main(String [] args){

        // Creating repositories to contain issues and agente
        IssueRepository issueRepository = new IssueRepository();
        AgentRepository agentRepository = new AgentRepository();

        // Creating agent and issue service
        AgentService agentService = new AgentServiceImpl(agentRepository);
        IssueService issueService = new IssueServiceImpl(issueRepository, agentRepository);

        // Save created issue in main
        Issue issue1 = issueService.createIssue("T1", "Payment Related", "Payment Failed", "My payment failed but money is debited","testUser1@test.com");
        Issue issue2 = issueService.createIssue("T2", "Mutual Fund Related", "Purchase Failed", "Unable to purchase Mutual Fund", "testUser2@test.com");
        Issue issue3 = issueService.createIssue("T3", "Payment Related", "Payment Failed", "My payment failed but money is debited", "testUser2@test.com");

        // Adding agents
        agentService.addAgent("agent1@test.com", "Agent 1", Arrays.asList("Payment Related", "Gold Related"));
        agentService.addAgent("agent2@test.com", "Agent 2", Arrays.asList("Mutual Fund Related"));

        // Assigning issue to agent
        try{
            agentService.assignIssue(issue1);
            agentService.assignIssue(issue2);
            agentService.assignIssue(issue3);
        } catch (InvalidInputException e){
            System.out.println(e.getMessage());
        }

        // Filtering issues by Mail
        try{
            issueService.getIssuesByEmail("testUser2@test.com");
        } catch (InvalidInputException e){
            System.out.println(e.getMessage());
        }

        // Filtering issues by Type
        issueService.getIssuesByType(IssueType.PAYMENT_RELATED);

        // Updating the issue
        try{
            issueService.updateIssue("I1", "In Progress", "Waiting for payment confirmation");
        } catch (InvalidInputException e){
            System.out.println(e.getMessage());
        }

        // Resolving the issue
        try{
            issueService.resolveIssue("I1", "PaymentFailed debited amount will get reversed");
        } catch (InvalidInputException e){
            System.out.println(e.getMessage());
        }

        // List of "RESOLVED" tickets per agent not the "OPEN" ones
        System.out.println("Agents with List of Resolved issues ");
        agentService.viewAgentsWorkHistory();
    }
}
