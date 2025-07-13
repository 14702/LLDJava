package com.CustomerIssueResolutionSystem;

import com.CustomerIssueResolutionSystem.enums.IssueType;
import com.CustomerIssueResolutionSystem.exceptions.InvalidInputException;
import com.CustomerIssueResolutionSystem.repository.AgentRepository;
import com.CustomerIssueResolutionSystem.repository.IssueRepository;
import com.CustomerIssueResolutionSystem.service.interfaces.AgentService;
import com.CustomerIssueResolutionSystem.service.interfaces.IssueService;
import com.CustomerIssueResolutionSystem.service.impl.AgentServiceImpl;
import com.CustomerIssueResolutionSystem.service.impl.IssueServiceImpl;
import com.CustomerIssueResolutionSystem.model.Issue;
import java.util.Arrays;

public class Main {
    public static void main(String [] args){
        // create repo - IssueRepo, AgentRepo
        // create service: AgentService, IssueService
        // create model - Issue, Agent;
        // create ENUM for IssueType and IssueStatus

        IssueRepository issueRepository = new IssueRepository();
        AgentRepository agentRepository = new AgentRepository();

        // Can swap any other AgentService Impl and use their methods
        AgentService agentService = new AgentServiceImpl(agentRepository);
        IssueService issueService = new IssueServiceImpl(issueRepository, agentRepository);

        // always have the handle for new issue
        Issue issue1 = issueService.createIssue("T1", "mutual fund related", "Purchase Failed", "Unable to purchase Mutual Fund", "testUser1@test");
        Issue issue2 = issueService.createIssue("T2", "payment related", "Payment Failed", "My payment failed but money is debited","testUser2@test");

        agentService.addAgent("agent1@test.com", "Agent 1", Arrays.asList("payment related", "gold related"));
        agentService.addAgent("agent2@test.com", "Agent 2", Arrays.asList("payment related", "mutual fund related"));

        try{
            agentService.assignIssue(issue1); // check for edge cases when agents arenot possible
        } catch (InvalidInputException e){
            System.out.println(e.getMessage());
        }

        try{
            issueService.getIssuesByMail("testUser1@test");
        } catch (InvalidInputException e){
            System.out.println(e.getMessage());
        }

        issueService.getIssuesByType(IssueType.PAYMENT_RELATED);

        try{
            issueService.updateIssue("I1", "in progress", "Waiting for payment confirmation");
        } catch (InvalidInputException e){
            System.out.println(e.getMessage());
        }

        try{
            issueService.resolveIssue("I1", "PaymentFailed debited amount will get reversed");
        } catch (InvalidInputException e){
            System.out.println(e.getMessage());
        }

        agentService.viewAgentsWorkHistory();
    }
}
