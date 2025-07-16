package com.CustomerIssueResolutionSystem.service.interfaces;

import com.CustomerIssueResolutionSystem.enums.IssueType;
import com.CustomerIssueResolutionSystem.exceptions.InvalidInputException;
import com.CustomerIssueResolutionSystem.model.Issue;

public interface IssueService {
    Issue createIssue(String transId, String issueType, String subject, String description, String customerEmail);
    void getIssuesByMail(String email) throws InvalidInputException;
    void getIssuesByType(IssueType issueType);
    void printIssue (Issue issue);
    void updateIssue(String issueId, String issueStatus, String description) throws InvalidInputException;
    void resolveIssue(String issueId, String description) throws InvalidInputException;
}
