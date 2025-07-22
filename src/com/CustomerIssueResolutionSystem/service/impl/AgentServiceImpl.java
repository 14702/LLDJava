package com.CustomerIssueResolutionSystem.service.impl;

import com.CustomerIssueResolutionSystem.exceptions.InvalidInputException;
import com.CustomerIssueResolutionSystem.model.Agent;
import com.CustomerIssueResolutionSystem.model.Issue;
import com.CustomerIssueResolutionSystem.repository.AgentRepository;
import com.CustomerIssueResolutionSystem.service.interfaces.AgentService;
import com.CustomerIssueResolutionSystem.strategy.interfaces.AgentAssignmentStrategy;
import com.CustomerIssueResolutionSystem.strategy.impl.SimpleAgentAssignmentStrategy;

import java.util.*;

public class AgentServiceImpl implements AgentService {
    AgentRepository agentRepository;
    AgentAssignmentStrategy agentAssignmentStrategy;
    public AgentServiceImpl(AgentRepository agentRepository, AgentAssignmentStrategy agentAssignmentStrategy){
        this.agentRepository = agentRepository;
        this.agentAssignmentStrategy = agentAssignmentStrategy;
    }

    //addAgent(“agent1@test.com”, “Agent 1”, Arrays.asList("Payment Related", "Gold Related"));
    //Agent A1 created
    @Override
    public void addAgent(String email, String name, List<String> expertiseList){
        Agent agent = new Agent(email, name, expertiseList);
        agentRepository.addAgentToRepository(agent.getAgentId(), agent);
        System.out.println("Agent "+ agent.getAgentId()+ " created");
    }

    //desc: viewAgentsWorkHistory() //A1 -> {I1, I3}, A2 -> {I2} Time Duration
    @Override
    public void viewAgentsWorkHistory(){
        for(Agent agent : agentRepository.getAgents().values()){
            System.out.println(agent.getAgentId() + " -> {" );
            for(String issueId : agent.getWorkingHistory()){
                System.out.println(issueId + " ");
            }
            System.out.println("}");
        }
    }

    // use strategy to get agent
    @Override
    public void assignIssue (Issue issue) throws InvalidInputException {
        Map<String, Agent> agents = agentRepository.getAgents();
        if(issue == null)
            throw new InvalidInputException("Issue cant be assigned as its not created yet");

        agentAssignmentStrategy.assignAgent(issue, agentRepository.getAgents());

        /*if(issue.getAgentID().isEmpty())
            throw new InvalidInputException("No expert agents are available");*/

    }

}
