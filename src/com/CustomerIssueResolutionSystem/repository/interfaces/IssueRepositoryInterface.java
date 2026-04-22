package com.CustomerIssueResolutionSystem.repository.interfaces;

import com.CustomerIssueResolutionSystem.model.Issue;
import java.util.Map;

public interface IssueRepositoryInterface {
    void addIssueToRepository(Issue issue);
    Issue getIssueById(String issueId);
    Map<String, Issue> getIssues();
    void removeIssueFromRepository(String issueId);
}
