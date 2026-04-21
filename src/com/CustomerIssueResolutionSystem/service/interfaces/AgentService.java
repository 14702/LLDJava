package com.CustomerIssueResolutionSystem.service.interfaces;
import com.CustomerIssueResolutionSystem.exceptions.AgentNotFoundException;
import com.CustomerIssueResolutionSystem.exceptions.InvalidInputException;

import java.util.List;

public interface AgentService {
    void addAgent(String email, String name, List<String> expertiseList);
    void viewAgentsWorkHistory();
    void assignIssue (String issueId) throws InvalidInputException;
    void deleteAgent(String agentId) throws AgentNotFoundException, InvalidInputException;
    void updateAgentExpertise(String agentId, List<String> newExpertiseList) 
        throws AgentNotFoundException, InvalidInputException;
}
