package com.AppVersionManagementSystem.model;

import java.util.Arrays;
import java.util.Objects;

public final class AppVersionDetails {
    private final Version version;
    private final byte[] file;
    private final AppMetaData appMetaData;

    public AppVersionDetails(Version version, byte[] file, AppMetaData appMetaData) {
        this.version = version;
        this.file = file != null ? Arrays.copyOf(file, file.length) : null;
        this.appMetaData = appMetaData;
    }

    public Version getVersion() {
        return version;
    }

    public byte[] getFile() {
        return file != null ? Arrays.copyOf(file, file.length) : null;
    }

    public AppMetaData getAppMetaData() {
        return appMetaData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppVersionDetails that = (AppVersionDetails) o;
        return Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version);
    }

    @Override
    public String toString() {
        return "AppVersionDetails{version=" + version + "}";
    }
}
