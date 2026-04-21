package com.AppVersionManagementSystem.repository.interfaces;

import com.AppVersionManagementSystem.model.Device;

import java.util.List;

public interface DeviceRepository {
    void addDevice(Device device);
    Device getById(String deviceId);
    List<Device> getAllDevices();
    boolean exists(String deviceId);
}
