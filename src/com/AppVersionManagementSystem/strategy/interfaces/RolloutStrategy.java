package com.AppVersionManagementSystem.strategy.interfaces;

import com.AppVersionManagementSystem.model.Device;

import java.util.List;

public interface RolloutStrategy {
    List<Device> getEligibleDevices();
    boolean isDeviceEligible(Device device);
}
