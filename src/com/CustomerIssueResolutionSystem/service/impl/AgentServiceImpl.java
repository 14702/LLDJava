package com.CustomerIssueResolutionSystem.service.impl;

import com.CustomerIssueResolutionSystem.enums.IssueStatus;
import com.CustomerIssueResolutionSystem.enums.IssueType;
import com.CustomerIssueResolutionSystem.exceptions.AgentNotFoundException;
import com.CustomerIssueResolutionSystem.exceptions.InvalidInputException;
import com.CustomerIssueResolutionSystem.model.Agent;
import com.CustomerIssueResolutionSystem.model.Issue;
import com.CustomerIssueResolutionSystem.repository.interfaces.AgentRepositoryInterface;
import com.CustomerIssueResolutionSystem.service.interfaces.AgentService;
import com.CustomerIssueResolutionSystem.strategy.interfaces.AgentAssignmentStrategy;

import com.CustomerIssueResolutionSystem.repository.interfaces.IssueRepositoryInterface;

import java.util.*;

public class AgentServiceImpl implements AgentService {
    AgentRepositoryInterface agentRepository;
    IssueRepositoryInterface issueRepository;
    AgentAssignmentStrategy agentAssignmentStrategy;

    public AgentServiceImpl(AgentRepositoryInterface agentRepository, IssueRepositoryInterface issueRepository, AgentAssignmentStrategy agentAssignmentStrategy){
        this.agentRepository = agentRepository;
        this.issueRepository = issueRepository;
        this.agentAssignmentStrategy = agentAssignmentStrategy;
    }

    @Override
    public void addAgent(String email, String name, List<String> expertiseList) {
        // ✅ FIX: Add validation
        if(email == null || email.trim().isEmpty())
            throw new RuntimeException("Email cannot be null or empty"); // % this work but not InvalidInputException
        if(name == null || name.trim().isEmpty())
            throw new RuntimeException("Name cannot be null or empty");
        if(expertiseList == null || expertiseList.isEmpty())
            throw new RuntimeException("Expertise list cannot be empty");
        
        try {
            Agent agent = new Agent(email, name, expertiseList);
            agentRepository.addAgentToRepository(agent.getAgentId(), agent);
            System.out.println("Agent "+ agent.getAgentId()+ " created");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to create agent: " + e.getMessage());
        }
    }

    @Override
    public void viewAgentsWorkHistory(){
        for(Agent agent : agentRepository.getAgents().values()){
            System.out.println(agent.getAgentId() + " -> {" + String.join(", ", agent.getWorkingHistory()) + "}");
        }
    }

    @Override
    public void assignIssue (String issueId) throws InvalidInputException {
        if(issueId == null || issueId.trim().isEmpty())
            throw new InvalidInputException("Issue ID cannot be null or empty");

        Issue issue = issueRepository.getIssueById(issueId);
        if(issue == null)
            throw new InvalidInputException("Issue not found for ID: " + issueId);

        agentAssignmentStrategy.assignAgent(issue, agentRepository.getAgents());
    }

    @Override
    public void deleteAgent(String agentId) throws AgentNotFoundException, InvalidInputException {
        // ✅ FIX: Null check before isEmpty()
        if(agentId == null || agentId.trim().isEmpty())
            throw new InvalidInputException("Agent ID must be provided to delete agent");

        Agent agent = agentRepository.getAgents().get(agentId);
        if(agent == null)
            throw new AgentNotFoundException("Agent with ID: " + agentId + " not found");

        // ✅ FIX: Complete reassignment logic
        if(agent.getCurrIssue() != null){
            Issue currentIssue = agent.getCurrIssue();
            currentIssue.setIssueStatus(IssueStatus.OPEN);
            currentIssue.setAgentID("");
            try{
                assignIssue(currentIssue.getIssueId());
            } catch (InvalidInputException e){
                System.out.println("Could not reassign issue " + currentIssue.getIssueId() + ": " + e.getMessage());
            }
        }

        // ✅ FIX: Reassign all waiting issues
        Queue<Issue> waitingList = agent.getWaitingIssuesList();
        while(!waitingList.isEmpty()){
            Issue waitingIssue = waitingList.poll();
            waitingIssue.setIssueStatus(IssueStatus.OPEN);
            waitingIssue.setAgentID("");
            try{
                assignIssue(waitingIssue.getIssueId());
            } catch (InvalidInputException e){
                System.out.println("Could not reassign waiting issue " + waitingIssue.getIssueId() + ": " + e.getMessage());
            }
        }

        // ✅ FIX: Actually remove agent from repository
        agentRepository.removeAgentFromRepository(agentId);
        System.out.println("Agent " + agentId + " deleted successfully and all issues reassigned");
    }

    @Override
    public void updateAgentExpertise(String agentId, List<String> newExpertiseList) 
            throws AgentNotFoundException, InvalidInputException {
        if(agentId == null || agentId.trim().isEmpty())
            throw new InvalidInputException("Agent ID cannot be null or empty");
        if(newExpertiseList == null || newExpertiseList.isEmpty())
            throw new InvalidInputException("Expertise list cannot be empty");

        Agent agent = agentRepository.getAgentById(agentId);
        
        // ✅ Check if current issue still matches expertise
        if(agent.getCurrIssue() != null) {
            Issue currIssue = agent.getCurrIssue();
            boolean canHandleCurrentIssue = newExpertiseList.stream()
                    .anyMatch(expertise -> currIssue.getIssueType()
                            .equals(IssueType.getIssueType(expertise)));
            
            if(!canHandleCurrentIssue) {
                // Reassign current issue
                currIssue.setIssueStatus(IssueStatus.OPEN);
                currIssue.setAgentID("");
                try{
                    assignIssue(currIssue.getIssueId());
                } catch (InvalidInputException e){
                    System.out.println("Could not reassign issue: " + e.getMessage());
                }
                agent.setCurrIssue(null);
            }
        }
        
        // ✅ Check waiting list and reassign incompatible issues
        Queue<Issue> waitingList = agent.getWaitingIssuesList();
        List<Issue> toRemove = new ArrayList<>();
        for(Issue issue : waitingList) {
            boolean canHandle = newExpertiseList.stream()
                    .anyMatch(expertise -> issue.getIssueType()
                            .equals(IssueType.getIssueType(expertise)));
            if(!canHandle) {
                toRemove.add(issue);
            }
        }
        
        for(Issue issue : toRemove) {
            waitingList.remove(issue);
            issue.setIssueStatus(IssueStatus.OPEN);
            issue.setAgentID("");
            try{
                assignIssue(issue.getIssueId());
            } catch (InvalidInputException e){
                System.out.println("Could not reassign waiting issue: " + e.getMessage());
            }
        }
        
        // ✅ Update expertise
        agent.getExpertiseList().clear();
        agent.getExpertiseList().addAll(newExpertiseList);
        System.out.println("Agent " + agentId + " expertise updated successfully");
    }
}
