package com.CustomerIssueResolutionSystem.enums;

public enum IssueType {
    PAYMENT_RELATED,
    MF_RELATED,
    GOLD_RELATED,
    INSURANCE_RELATED;

    public static IssueType getIssueType(String issuetype){
        switch(issuetype){
            case "Payment Related" : return PAYMENT_RELATED;
            case "Mutual Fund Related" : return MF_RELATED;
            case "Gold Related" : return GOLD_RELATED;
            case "Insurance Related" : return INSURANCE_RELATED;
            default : throw new IllegalArgumentException("Unknown issue type: " + issuetype);
        }
    }

    public static String getIssueDescription (IssueType issueType){
        switch(issueType){
            case PAYMENT_RELATED : return "Payment Related";
            case MF_RELATED: return "Mutual Fund Related";
            case GOLD_RELATED: return "Gold Related";
            case INSURANCE_RELATED: return "Insurance Related";
            default : throw new IllegalArgumentException("Issue type not found");
        }
    }
}
