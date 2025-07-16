package com.CustomerIssueResolutionSystem.enums;

public enum IssueStatus {
    WAITING,
    IN_PROGRESS,
    ASSIGNED,
    RESOLVED;

    public static IssueStatus getIssueStatus(String issueStatus){
        switch(issueStatus){
            case "waiting": return WAITING;
            case "in progress" : return IN_PROGRESS;
            case "assigned" : return ASSIGNED;
            case "resolved" : return RESOLVED;
            default : throw new IllegalArgumentException("Unknown issue status: " + issueStatus);
        }
    }
}
