package com.CustomerIssueResolutionSystem.strategy.impl;

import com.CustomerIssueResolutionSystem.enums.IssueType;
import com.CustomerIssueResolutionSystem.model.Agent;
import com.CustomerIssueResolutionSystem.model.Issue;
import com.CustomerIssueResolutionSystem.strategy.interfaces.AgentAssignmentStrategy;

import java.util.Map;

public class SimpleAgentAssignmentStrategy implements AgentAssignmentStrategy {

    @Override
    public void assignAgent (Issue issue, Map<String, Agent> agents){

        for( Agent agent : agents.values()){
            if(agent.getCurrIssue() == null){
                for(String expertise : agent.getExpertiseList()){
                    if(issue.getIssueType().equals(IssueType.getIssueType(expertise))){
                        // Assign agent for issue
                        issue.setAgentID(agent.getAgentId());
                        agent.setCurrIssue(issue);
                        System.out.println("Issue "+ issue.getIssueId() +  " assigned to agent: " + agent.getAgentId() );
                    }
                }
            }
        }

    }
}


