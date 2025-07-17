package com.AppVersionManagementSystem.strategy.interfaces;

import com.AppVersionManagementSystem.model.AppVersionDetails;

public interface RolloutStrategy {
    void rollout(AppVersionDetails appVersionDetails);
}