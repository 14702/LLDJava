package com.AppVersionManagementSystem.model;

import java.util.Objects;

public final class Version implements Comparable<Version> {
    private final int majorVersion;
    private final int minorVersion;
    private final int patchVersion;

    public Version(int majorVersion, int minorVersion, int patchVersion) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.patchVersion = patchVersion;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public int getPatchVersion() {
        return patchVersion;
    }

    @Override
    public int compareTo(Version other) {
        if (this.majorVersion != other.majorVersion) {
            return Integer.compare(this.majorVersion, other.majorVersion);
        }
        if (this.minorVersion != other.minorVersion) {
            return Integer.compare(this.minorVersion, other.minorVersion);
        }
        return Integer.compare(this.patchVersion, other.patchVersion);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return majorVersion == version.majorVersion
                && minorVersion == version.minorVersion
                && patchVersion == version.patchVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(majorVersion, minorVersion, patchVersion);
    }

    @Override
    public String toString() {
        return majorVersion + "." + minorVersion + "." + patchVersion;
    }
}
