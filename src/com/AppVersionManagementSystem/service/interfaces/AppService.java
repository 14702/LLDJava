package com.AppVersionManagementSystem.service.interfaces;

import com.AppVersionManagementSystem.enums.TaskType;
import com.AppVersionManagementSystem.model.AppVersionDetails;
import com.AppVersionManagementSystem.model.Device;
import com.AppVersionManagementSystem.model.Version;

public interface AppService {
    void uploadNewVersion(AppVersionDetails appVersionDetails);
    byte[] createUpdatePatch(Version fromVersion, Version toVersion);
    void releaseVersion(AppVersionDetails appVersionDetails);
    boolean isAppVersionSupported(Device device, AppVersionDetails appVersionDetails);
    boolean checkForInstall(Device device);
    boolean checkForUpdates(Device device);
    void executeTask(Device device, TaskType taskType);
}
