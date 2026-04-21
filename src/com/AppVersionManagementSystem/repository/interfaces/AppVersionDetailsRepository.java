package com.AppVersionManagementSystem.repository.interfaces;

import com.AppVersionManagementSystem.model.AppVersionDetails;
import com.AppVersionManagementSystem.model.Version;

public interface AppVersionDetailsRepository {
    void save(AppVersionDetails appVersionDetails);
    AppVersionDetails getByVersion(Version version);
    AppVersionDetails getLatestVersion();
    boolean exists(Version version);
}
