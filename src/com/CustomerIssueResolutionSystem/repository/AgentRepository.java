package com.CustomerIssueResolutionSystem.repository;
import com.CustomerIssueResolutionSystem.model.Agent;
import java.util.*;

public class AgentRepository {
    Map<String, Agent> agents;  // {"A1", agent}

    public AgentRepository(){
        this.agents = new HashMap<>();
    }
    public void addAgentToRepository(String agentId, Agent agent){
        agents.put(agentId, agent);
    }

    public Agent getAgentById(String agentId){
        if(agents.containsKey(agentId))
                return agents.get(agentId);
        else
            throw new IllegalArgumentException("Agent with id " + agentId + "not found");
    }

    public Map<String, Agent> getAgents(){
        return this.agents;
    }

}
