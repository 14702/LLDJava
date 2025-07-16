package com.CustomerIssueResolutionSystem.strategy.interfaces;

import com.CustomerIssueResolutionSystem.model.Issue;
import com.CustomerIssueResolutionSystem.model.Agent;
import java.util.Map;

public interface AgentAssignmentStrategy {
    void assignAgent (Issue issue, Map<String, Agent> agents);
}
