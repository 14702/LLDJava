package com.AppVersionManagementSystem.model;

import java.util.Objects;

public final class AppMetaData {
    private final Version minimumSupportedAndroidVersion;
    private final Version minimumSupportedIOSVersion;
    private final int minimumRamInMB;
    private final int minimumStorageInMB;

    public AppMetaData(Version minimumSupportedAndroidVersion, Version minimumSupportedIOSVersion,
                       int minimumRamInMB, int minimumStorageInMB) {
        this.minimumSupportedAndroidVersion = minimumSupportedAndroidVersion;
        this.minimumSupportedIOSVersion = minimumSupportedIOSVersion;
        this.minimumRamInMB = minimumRamInMB;
        this.minimumStorageInMB = minimumStorageInMB;
    }

    public Version getMinimumSupportedAndroidVersion() {
        return minimumSupportedAndroidVersion;
    }

    public Version getMinimumSupportedIOSVersion() {
        return minimumSupportedIOSVersion;
    }

    public int getMinimumRamInMB() {
        return minimumRamInMB;
    }

    public int getMinimumStorageInMB() {
        return minimumStorageInMB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppMetaData that = (AppMetaData) o;
        return minimumRamInMB == that.minimumRamInMB
                && minimumStorageInMB == that.minimumStorageInMB
                && Objects.equals(minimumSupportedAndroidVersion, that.minimumSupportedAndroidVersion)
                && Objects.equals(minimumSupportedIOSVersion, that.minimumSupportedIOSVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minimumSupportedAndroidVersion, minimumSupportedIOSVersion,
                minimumRamInMB, minimumStorageInMB);
    }

    @Override
    public String toString() {
        return "AppMetaData{minAndroid=" + minimumSupportedAndroidVersion
                + ", minIOS=" + minimumSupportedIOSVersion
                + ", minRAM=" + minimumRamInMB + "MB"
                + ", minStorage=" + minimumStorageInMB + "MB}";
    }
}
