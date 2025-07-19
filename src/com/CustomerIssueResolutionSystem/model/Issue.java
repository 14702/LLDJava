package com.CustomerIssueResolutionSystem.model;

import com.CustomerIssueResolutionSystem.enums.IssueType;
import com.CustomerIssueResolutionSystem.enums.IssueStatus;

public class Issue {
    public static Integer idCounter = 1;
    String transId;
    String issueId;
    IssueType issueType;
    IssueStatus issueStatus;
    String subject;
    String description;
    String customerEmail;
    String resolution;
    String agentID;

    public Issue(String transId, IssueType issueType, String subject, String description, String customerEmail){
        this.transId = transId;
        this.issueType = issueType;
        this.subject = subject;
        this.description = description;
        this.customerEmail = customerEmail;
        this.issueId = "I" + Issue.idCounter++;
        this.issueStatus = IssueStatus.OPEN;
        this.resolution = "";
        this.agentID = "";
    }

    public String getIssueId(){return this.issueId;}
    public String getTransId(){return this.transId;}
    public String getSubject(){return this.subject;}
    public String getDescription(){return this.description;}
    public String getCustomerEmail(){return this.customerEmail;}
    public IssueType getIssueType(){return this.issueType;}
    public IssueStatus getIssueStatus() {return this.issueStatus;}
    public void setIssueStatus(IssueStatus status) {this.issueStatus = status;}
    public void setResolution(String resolution) {this.resolution = resolution;}
    public void setAgentID(String agentID) {this.agentID = agentID;}
    public String getAgentID() {return this.agentID;}
    public void setDescription(String description) {this.description = description;}
}
