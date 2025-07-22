package com.CustomerIssueResolutionSystem.enums;

public enum IssueStatus {
    OPEN,
    IN_PROGRESS,
    ASSIGNED,
    RESOLVED;

    public static IssueStatus getIssueStatus(String issueStatus){
        switch(issueStatus){
            case "Open": return OPEN;
            // case "Waiting": return WAITING; can remove this, Only 4 is needed, @creation - OPEN, @agentAssign - ASSIGNED, @updateissue - INPROGRESS , RESOLVED
            case "In Progress" : return IN_PROGRESS;
            case "Assigned" : return ASSIGNED;
            case "Resolved" : return RESOLVED;
            default : throw new IllegalArgumentException("Unknown issue status: " + issueStatus);
        }
    }
}
