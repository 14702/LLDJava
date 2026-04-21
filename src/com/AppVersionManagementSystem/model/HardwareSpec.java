package com.AppVersionManagementSystem.model;

import java.util.Objects;

public final class HardwareSpec {
    private final int ramInMB;
    private final int storageInMB;
    private final String deviceModel;

    public HardwareSpec(int ramInMB, int storageInMB, String deviceModel) {
        this.ramInMB = ramInMB;
        this.storageInMB = storageInMB;
        this.deviceModel = deviceModel;
    }

    public int getRamInMB() {
        return ramInMB;
    }

    public int getStorageInMB() {
        return storageInMB;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HardwareSpec that = (HardwareSpec) o;
        return ramInMB == that.ramInMB && storageInMB == that.storageInMB
                && Objects.equals(deviceModel, that.deviceModel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ramInMB, storageInMB, deviceModel);
    }

    @Override
    public String toString() {
        return "HardwareSpec{ram=" + ramInMB + "MB, storage=" + storageInMB
                + "MB, model='" + deviceModel + "'}";
    }
}
