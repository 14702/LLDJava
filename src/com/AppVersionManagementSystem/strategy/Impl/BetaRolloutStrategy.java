package com.AppVersionManagementSystem.strategy.impl;

import com.AppVersionManagementSystem.model.Device;
import com.AppVersionManagementSystem.repository.interfaces.DeviceRepository;
import com.AppVersionManagementSystem.strategy.interfaces.RolloutStrategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BetaRolloutStrategy implements RolloutStrategy {

    private final DeviceRepository deviceRepository;
    private final Set<String> betaDeviceIds;

    public BetaRolloutStrategy(DeviceRepository deviceRepository, Set<String> betaDeviceIds) {
        this.deviceRepository = deviceRepository;
        this.betaDeviceIds = Collections.unmodifiableSet(new HashSet<>(betaDeviceIds));
    }

    @Override
    public List<Device> getEligibleDevices() {
        return deviceRepository.getAllDevices()
                .stream()
                .filter(this::isDeviceEligible)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isDeviceEligible(Device device) {
        return device.isBetaEnabled() || betaDeviceIds.contains(device.getId());
    }
}
