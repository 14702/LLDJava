package com.CustomerIssueResolutionSystem.enums;

public enum IssueStatus {
    OPEN,
    WAITING,
    IN_PROGRESS,
    ASSIGNED,
    RESOLVED;

    public static IssueStatus getIssueStatus(String issueStatus){
        switch(issueStatus){
            case "Open": return OPEN;
            case "Waiting": return WAITING;
            case "In Progress" : return IN_PROGRESS;
            case "Assigned" : return ASSIGNED;
            case "Resolved" : return RESOLVED;
            default : throw new IllegalArgumentException("Unknown issue status: " + issueStatus);
        }
    }
}
