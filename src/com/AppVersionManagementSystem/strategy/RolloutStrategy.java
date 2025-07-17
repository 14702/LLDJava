package com.AppVersionManagementSystem.strategy;

import com.AppVersionManagementSystem.model.AppVersionDetails;

public interface RolloutStrategy {
    void rollout(AppVersionDetails appVersionDetails);
}