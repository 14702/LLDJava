package com.CustomerIssueResolutionSystem.strategy.impl;

import com.CustomerIssueResolutionSystem.enums.IssueStatus;
import com.CustomerIssueResolutionSystem.enums.IssueType;
import com.CustomerIssueResolutionSystem.model.Agent;
import com.CustomerIssueResolutionSystem.model.Issue;
import com.CustomerIssueResolutionSystem.strategy.interfaces.AgentAssignmentStrategy;

import java.util.Map;

public class SimpleAgentAssignmentStrategy implements AgentAssignmentStrategy {

    @Override
    public void assignAgent(Issue issue, Map<String, Agent> agents){
        if(issue == null) throw new IllegalArgumentException("Issue cannot be null");
        
        // ✅ FIX: First pass - assign to free agent
        for(Agent agent : agents.values()){
            synchronized(agent){  // ✅ Keep synchronized block
                if(agent.getCurrIssue() == null){
                    boolean canHandle = agent.getExpertiseList().stream()
                            .anyMatch(expertise -> 
                                issue.getIssueType().equals(IssueType.getIssueType(expertise)));
                    
                    if(canHandle){
                        // ✅ FIX: All state changes INSIDE synchronized block
                        issue.setAgentID(agent.getAgentId());
                        issue.setIssueStatus(IssueStatus.ASSIGNED);
                        agent.setCurrIssue(issue);
                        agent.addToWorkingHistory(issue.getIssueId());
                        System.out.println("Issue "+ issue.getIssueId() +  " assigned to agent: " + agent.getAgentId());
                        return;  // ✅ Exit after successful assignment
                    }
                }
            }
        }
        
        // ✅ FIX: Second pass - add to waiting queue
        for(Agent agent : agents.values()){
            synchronized(agent){
                boolean canHandle = agent.getExpertiseList().stream()
                        .anyMatch(expertise -> 
                            issue.getIssueType().equals(IssueType.getIssueType(expertise)));
                
                if(canHandle){
                    agent.addToWaitingIssuesList(issue);
                    System.out.println("Issue "+ issue.getIssueId() +  " added to waiting list for agent: " + agent.getAgentId());
                    return;  // ✅ Exit after adding to queue
                }
            }
        }
        
        // ✅ FIX: Edge case - no agents available
        System.out.println("⚠️ WARNING: Issue "+ issue.getIssueId() +  " could not be assigned - no agents with required expertise available");
    }
}


