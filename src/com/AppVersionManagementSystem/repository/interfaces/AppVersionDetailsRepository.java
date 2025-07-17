package com.AppVersionManagementSystem.repository.interfaces;

import com.AppVersionManagementSystem.model.AppVersionDetails;
import com.AppVersionManagementSystem.model.Version;

public interface AppVersionDetailsRepository {
    void uploadNewVersion(AppVersionDetails appVersionDetails);
    Version getLatestVersion();
}