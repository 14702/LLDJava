package com.CustomerIssueResolutionSystem.model;

import com.CustomerIssueResolutionSystem.enums.IssueType;
import com.CustomerIssueResolutionSystem.enums.IssueStatus;
import java.util.concurrent.atomic.AtomicInteger;

public class Issue {
    // ✅ FIX: Use AtomicInteger for thread-safe counter
    private static final AtomicInteger idCounter = new AtomicInteger(1);
    
    String transId;
    String issueId;
    IssueType issueType;
    IssueStatus issueStatus;
    String subject;
    String description;
    String customerEmail;
    String resolution;
    String agentID;

    public Issue(String transId, IssueType issueType, String subject, 
                 String description, String customerEmail){
        // ✅ FIX: Add null checks for all parameters
        if(transId == null || transId.trim().isEmpty()) 
            throw new IllegalArgumentException("Transaction ID cannot be null or empty");
        if(issueType == null) 
            throw new IllegalArgumentException("Issue type cannot be null");
        if(subject == null || subject.trim().isEmpty()) 
            throw new IllegalArgumentException("Subject cannot be null or empty");
        if(description == null || description.trim().isEmpty()) 
            throw new IllegalArgumentException("Description cannot be null or empty");
        if(customerEmail == null || customerEmail.trim().isEmpty()) 
            throw new IllegalArgumentException("Customer email cannot be null or empty");
        if(!isValidEmail(customerEmail))
            throw new IllegalArgumentException("Invalid email format: " + customerEmail);
            
        this.transId = transId.trim();
        this.issueType = issueType;
        this.subject = subject.trim();
        this.description = description.trim();
        this.customerEmail = customerEmail.trim();
        this.issueId = "I" + idCounter.getAndIncrement();  // ✅ Thread-safe increment
        this.issueStatus = IssueStatus.OPEN;
        this.resolution = "";
        this.agentID = "";
    }

    // ✅ FIX: Add email validation helper
    private static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
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
