package com.AppVersionManagementSystem.repository.impl;

import com.AppVersionManagementSystem.model.AppVersionDetails;
import com.AppVersionManagementSystem.model.Version;
import com.AppVersionManagementSystem.repository.interfaces.AppVersionDetailsRepository;

import java.util.concurrent.ConcurrentHashMap;

public class LocalAppVersionDetailsRepository implements AppVersionDetailsRepository {

    private final ConcurrentHashMap<Version, AppVersionDetails> appCache = new ConcurrentHashMap<>();
    private volatile AppVersionDetails latestVersion = null;
    private final Object latestVersionLock = new Object();

    @Override
    public void save(AppVersionDetails appVersionDetails) {
        appCache.put(appVersionDetails.getVersion(), appVersionDetails);
        synchronized (latestVersionLock) {
            if (latestVersion == null
                    || appVersionDetails.getVersion().compareTo(latestVersion.getVersion()) > 0) {
                latestVersion = appVersionDetails;
            }
        }
    }

    @Override
    public AppVersionDetails getByVersion(Version version) {
        return appCache.get(version);
    }

    @Override
    public AppVersionDetails getLatestVersion() {
        return latestVersion;
    }

    @Override
    public boolean exists(Version version) {
        return appCache.containsKey(version);
    }
}
