package com.CustomerIssueResolutionSystem.repository.impl;
import com.CustomerIssueResolutionSystem.repository.interfaces.AgentRepositoryInterface;
import com.CustomerIssueResolutionSystem.model.Agent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AgentRepository implements AgentRepositoryInterface {
    Map<String, Agent> agents;

    public AgentRepository(){
        this.agents = new ConcurrentHashMap<>();
    }

    @Override
    public void addAgentToRepository(String agentId, Agent agent){
        if(agentId == null || agentId.isEmpty() || agent == null)
            throw new IllegalArgumentException("Agent ID and Agent object cannot be null");
        agents.put(agentId, agent);
    }

    @Override
    public Agent getAgentById(String agentId){
        if(agentId == null || agentId.isEmpty())
            throw new IllegalArgumentException("Agent ID cannot be null or empty");
        if(!agents.containsKey(agentId))
            throw new IllegalArgumentException("Agent with id " + agentId + " not found");
        return agents.get(agentId);
    }

    @Override
    public Map<String, Agent> getAgents(){
        return new ConcurrentHashMap<>(this.agents);
    }

    @Override
    public void removeAgentFromRepository(String agentId) {
        if(agentId == null || agentId.isEmpty())
            throw new IllegalArgumentException("Agent ID cannot be null");
        agents.remove(agentId);
    }
}
