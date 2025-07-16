package com.CustomerIssueResolutionSystem.service.interfaces;
import com.CustomerIssueResolutionSystem.exceptions.InvalidInputException;
import com.CustomerIssueResolutionSystem.model.Issue;

import java.util.List;

public interface AgentService {
    void addAgent(String email, String name, List<String> expertiseList);
    void viewAgentsWorkHistory();
    void assignIssue (Issue issue) throws InvalidInputException;
}
