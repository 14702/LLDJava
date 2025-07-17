package com.AppVersionManagementSystem.service.interfaces;

import com.AppVersionManagementSystem.model.AppVersionDetails;
import com.AppVersionManagementSystem.model.Device;

public interface AppService {
    void uploadNewVersion(AppVersionDetails appVersionDetails);
    String createUpdatePatch(AppVersionDetails oldVersion, AppVersionDetails newVersion);
    void releaseVersion(AppVersionDetails newVersion);
    boolean checkForInstall(Device device, AppVersionDetails appVersionDetails);
    boolean checkForUpdates(Device device, AppVersionDetails appVersionDetails);
}