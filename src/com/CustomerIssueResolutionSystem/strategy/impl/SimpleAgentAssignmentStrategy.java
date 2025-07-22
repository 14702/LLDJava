package com.CustomerIssueResolutionSystem.strategy.impl;

import com.CustomerIssueResolutionSystem.enums.IssueStatus;
import com.CustomerIssueResolutionSystem.enums.IssueType;
import com.CustomerIssueResolutionSystem.model.Agent;
import com.CustomerIssueResolutionSystem.model.Issue;
import com.CustomerIssueResolutionSystem.strategy.interfaces.AgentAssignmentStrategy;

import java.util.Map;

public class SimpleAgentAssignmentStrategy implements AgentAssignmentStrategy {

    @Override
    public void assignAgent (Issue issue, Map<String, Agent> agents){

        for( Agent agent : agents.values()){
            // can use Synchronized(agent) if multiple threads are used
            if(agent.getCurrIssue() == null){   // agent is free -> check expertise
                for(String expertise : agent.getExpertiseList()){
                    if(issue.getIssueType().equals(IssueType.getIssueType(expertise))){
                        // Assign agent for issue
                        issue.setAgentID(agent.getAgentId());
                        issue.setIssueStatus(IssueStatus.ASSIGNED);                                 // ++ Assigned state
                        agent.setCurrIssue(issue);
                        // add the issue to agent working history
                        agent.addToWorkingHistory(issue.getIssueId());                              // ++ added to working history
                        System.out.println("Issue "+ issue.getIssueId() +  " assigned to agent: " + agent.getAgentId() );
                    }
                }
            }
        }
        // no agent is found, get the first agent with expertise and assign issue to its waiting
        if(issue.getAgentID().isEmpty()){
            for( Agent agent : agents.values()){
                for(String expertise : agent.getExpertiseList()){
                    if(issue.getIssueType().equals(IssueType.getIssueType(expertise))){
                        // Assign to agent waiting list agent for issue
                        agent.addToWaitingIssuesList(issue);
                        System.out.println("Issue "+ issue.getIssueId() +  " added to waiting list for  agent: " + agent.getAgentId() );
                    }
                }
            }
        }
    }
}


