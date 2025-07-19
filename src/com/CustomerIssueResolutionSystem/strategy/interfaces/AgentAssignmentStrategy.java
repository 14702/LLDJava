package com.CustomerIssueResolutionSystem.strategy.interfaces;

import com.CustomerIssueResolutionSystem.model.Issue;
import com.CustomerIssueResolutionSystem.model.Agent;
import java.util.Map;

public interface AgentAssignmentStrategy {
    void assignAgent (Issue issue, Map<String, Agent> agents);
    // If no agents are available then based on the strategy, loop again through agents and add to the agents waiting list 
}
