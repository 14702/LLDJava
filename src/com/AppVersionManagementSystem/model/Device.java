package com.AppVersionManagementSystem.model;

import com.AppVersionManagementSystem.enums.DeviceType;

import java.util.Objects;

public final class Device {
    private final String id;
    private final boolean betaEnabled;
    private volatile Version appVersion;
    private final DeviceType deviceType;
    private final Version osVersion;
    private final HardwareSpec hardwareSpec;

    public Device(String id, boolean betaEnabled, Version appVersion,
                  DeviceType deviceType, Version osVersion, HardwareSpec hardwareSpec) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Device id cannot be null or blank");
        }
        if (deviceType == null) {
            throw new IllegalArgumentException("DeviceType cannot be null");
        }
        if (osVersion == null) {
            throw new IllegalArgumentException("OS version cannot be null");
        }
        if (hardwareSpec == null) {
            throw new IllegalArgumentException("HardwareSpec cannot be null");
        }
        this.id = id;
        this.betaEnabled = betaEnabled;
        this.appVersion = appVersion;
        this.deviceType = deviceType;
        this.osVersion = osVersion;
        this.hardwareSpec = hardwareSpec;
    }

    public String getId() {
        return id;
    }

    public boolean isBetaEnabled() {
        return betaEnabled;
    }

    public Version getAppVersion() {
        return appVersion;
    }

    public synchronized void setAppVersion(Version appVersion) {
        this.appVersion = appVersion;
    }

    public Version getOsVersion() {
        return osVersion;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public HardwareSpec getHardwareSpec() {
        return hardwareSpec;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(id, device.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Device{id='" + id + "', type=" + deviceType
                + ", os=" + osVersion + ", hw=" + hardwareSpec
                + ", app=" + appVersion + "}";
    }
}
