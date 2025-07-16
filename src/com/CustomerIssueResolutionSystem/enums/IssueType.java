package com.CustomerIssueResolutionSystem.enums;

public enum IssueType {
    PAYMENT_RELATED,
    MF_RELATED,
    GOLD_RELATED,
    INSURANCE_RELATED;

    public static IssueType getIssueType(String issuetype){
        switch(issuetype){
            case "payment related" : return PAYMENT_RELATED;
            case "mutual fund related" : return MF_RELATED;
            case "gold related" : return GOLD_RELATED;
            case "insurance related" : return INSURANCE_RELATED;
            default : throw new IllegalArgumentException("Unknown issue type: " + issuetype);
        }
    }

    public static String getIssueDescription (IssueType issueType){
        switch(issueType){
            case PAYMENT_RELATED : return "payment related";
            case MF_RELATED: return "mutual fund related";
            case GOLD_RELATED: return "gold related";
            case INSURANCE_RELATED: return "insurance related";
            default : throw new IllegalArgumentException("Issue type not found");
        }
    }
}
