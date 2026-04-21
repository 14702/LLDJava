package com.AppVersionManagementSystem.strategy.impl;

import com.AppVersionManagementSystem.model.Device;
import com.AppVersionManagementSystem.repository.interfaces.DeviceRepository;
import com.AppVersionManagementSystem.strategy.interfaces.RolloutStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class PercentageRolloutStrategy implements RolloutStrategy {

    private final DeviceRepository deviceRepository;
    private final int percentage;
    private final Set<String> eligibleDeviceIds = Collections.synchronizedSet(new HashSet<>());
    private volatile boolean computed = false;

    public PercentageRolloutStrategy(DeviceRepository deviceRepository, int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        this.deviceRepository = deviceRepository;
        this.percentage = percentage;
    }

    @Override
    public List<Device> getEligibleDevices() {
        computeEligibleSet();
        return deviceRepository.getAllDevices()
                .stream()
                .filter(this::isDeviceEligible)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isDeviceEligible(Device device) {
        computeEligibleSet();
        return eligibleDeviceIds.contains(device.getId());
    }

    private synchronized void computeEligibleSet() {
        if (computed) return;
        List<Device> allDevices = new ArrayList<>(deviceRepository.getAllDevices());
        Collections.shuffle(allDevices, ThreadLocalRandom.current());
        int count = (int) Math.ceil(allDevices.size() * percentage / 100.0);
        allDevices.stream()
                .limit(count)
                .forEach(d -> eligibleDeviceIds.add(d.getId()));
        computed = true;
    }
}
