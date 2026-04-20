package com.CustomerIssueResolutionSystem.enums;

public enum IssueStatus {
    OPEN("Open"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved");

    private final String displayName;

    IssueStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // ✅ FIX: Better error handling
    public static IssueStatus getIssueStatus(String issueStatus){
        if(issueStatus == null || issueStatus.trim().isEmpty())
            throw new IllegalArgumentException("Issue status cannot be null or empty");
        
        for(IssueStatus status : IssueStatus.values()){
            if(status.displayName.equalsIgnoreCase(issueStatus.trim())){
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown issue status: " + issueStatus);
    }
}
