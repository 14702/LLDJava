package com.AppVersionManagementSystem.repository.impl;

import com.AppVersionManagementSystem.model.Device;
import com.AppVersionManagementSystem.repository.interfaces.DeviceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LocalDeviceRepository implements DeviceRepository {

    private final ConcurrentHashMap<String, Device> deviceCache = new ConcurrentHashMap<>();

    @Override
    public void addDevice(Device device) {
        deviceCache.put(device.getId(), device);
    }

    @Override
    public Device getById(String deviceId) {
        return deviceCache.get(deviceId);
    }

    @Override
    public List<Device> getAllDevices() {
        return new ArrayList<>(deviceCache.values());
    }

    @Override
    public boolean exists(String deviceId) {
        return deviceCache.containsKey(deviceId);
    }
}
