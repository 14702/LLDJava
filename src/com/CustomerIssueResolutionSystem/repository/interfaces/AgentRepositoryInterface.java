package com.CustomerIssueResolutionSystem.repository.interfaces;
import com.CustomerIssueResolutionSystem.model.Agent;
import java.util.Map;

public interface AgentRepositoryInterface {
    void addAgentToRepository(String agentId, Agent agent);
    Agent getAgentById(String agentId) throws IllegalArgumentException;
    Map<String, Agent> getAgents();
    void removeAgentFromRepository(String agentId);  // ✅ NEW: Add this method
}