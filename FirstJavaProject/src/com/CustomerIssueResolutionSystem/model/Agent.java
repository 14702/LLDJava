package com.CustomerIssueResolutionSystem.model;
import java.util.*;

public class Agent {
    public static Integer agentCtr = 1;
    String agentId;
    String email;
    String name;
    List<String> expertiseList;
    Issue currIssue;
    List<String> workingHistory;        // {"I1", "I2", "I3"... }

    public Agent (String email, String name, List<String> expertiseList){
        this.agentId = "A" + Agent.agentCtr++;
        this.email = email;
        this.name = name ;
        this.expertiseList = expertiseList;
        this.currIssue = null;
        this.workingHistory = new ArrayList<>();
    }

    public List<String> getWorkingHistory(){
        return this.workingHistory;
    }

    public void addToWorkingHistory(String issueId){
        this.workingHistory.add(issueId);
    }

    public List<String> getExpertiseList(){
        return this.expertiseList;
    }

    public Issue getCurrIssue(){
        return this.currIssue;
    }
    public void setCurrIssue(Issue currIssue){
        this.currIssue = currIssue;
    }

    public String getAgentId(){
        return this.agentId;
    }
}
