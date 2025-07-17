package com.AppVersionManagementSystem.repository;

import com.AppVersionManagementSystem.model.AppVersionDetails;
import com.AppVersionManagementSystem.model.Device;

import java.util.List;

public interface DeviceRepository {
    void update(Device device, AppVersionDetails appVersionDetails, String diff);
    void addDevice(Device device);
    boolean checkForInstall(Device device, AppVersionDetails appVersionDetails);
    boolean checkForUpdates(Device device, AppVersionDetails appVersionDetails);
    List<Device> getAllDevices();
}