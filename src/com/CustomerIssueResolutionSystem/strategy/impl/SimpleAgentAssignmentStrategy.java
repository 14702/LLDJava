package com.CustomerIssueResolutionSystem.strategy.impl;

import com.CustomerIssueResolutionSystem.enums.IssueStatus;
import com.CustomerIssueResolutionSystem.model.Agent;
import com.CustomerIssueResolutionSystem.model.Issue;
import com.CustomerIssueResolutionSystem.strategy.interfaces.AgentAssignmentStrategy;

import java.util.Map;

public class SimpleAgentAssignmentStrategy implements AgentAssignmentStrategy {

    @Override
    public void assignAgent(Issue issue, Map<String, Agent> agents){
        if(issue == null) throw new IllegalArgumentException("Issue cannot be null");

        for(Agent agent : agents.values()){
            synchronized(agent){
                if(agent.getCurrIssue() == null){
                    issue.setAgentID(agent.getAgentId());
                    issue.setIssueStatus(IssueStatus.ASSIGNED);
                    agent.setCurrIssue(issue);
                    agent.addToWorkingHistory(issue.getIssueId());
                    System.out.println("Issue "+ issue.getIssueId() + " assigned to agent " + agent.getAgentId());
                    return;
                }
            }
        }

        for(Agent agent : agents.values()){
            synchronized(agent){
                agent.addToWaitingIssuesList(issue);
                agent.addToWorkingHistory(issue.getIssueId());
                System.out.println("Issue "+ issue.getIssueId() + " added to waitlist of Agent " + agent.getAgentId());
                return;
            }
        }

        System.out.println("No agents available for issue " + issue.getIssueId());
    }
}


