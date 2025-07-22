package com.CustomerIssueResolutionSystem.exceptions;

public class IssueNotFoundException extends RuntimeException {
    public IssueNotFoundException(String message) {
        super(message);
    }
}
