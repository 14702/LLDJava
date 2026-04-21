package com.AppVersionManagementSystem.service.impl;

import com.AppVersionManagementSystem.enums.DeviceType;
import com.AppVersionManagementSystem.enums.TaskType;
import com.AppVersionManagementSystem.model.App;
import com.AppVersionManagementSystem.model.AppVersionDetails;
import com.AppVersionManagementSystem.model.Device;
import com.AppVersionManagementSystem.model.Version;
import com.AppVersionManagementSystem.repository.interfaces.AppVersionDetailsRepository;
import com.AppVersionManagementSystem.service.interfaces.AppService;
import com.AppVersionManagementSystem.service.interfaces.FileService;
import com.AppVersionManagementSystem.strategy.interfaces.RolloutStrategy;

import java.util.List;

public class ConcreteAppService implements AppService {

    private final App app;
    private final AppVersionDetailsRepository appVersionDetailsRepository;
    private final FileService fileService;
    private final RolloutStrategy rolloutStrategy;

    public ConcreteAppService(App app,
                              AppVersionDetailsRepository appVersionDetailsRepository,
                              FileService fileService,
                              RolloutStrategy rolloutStrategy) {
        this.app = app;
        this.appVersionDetailsRepository = appVersionDetailsRepository;
        this.fileService = fileService;
        this.rolloutStrategy = rolloutStrategy;
    }

    @Override
    public synchronized void uploadNewVersion(AppVersionDetails appVersionDetails) {
        if (appVersionDetails == null) {
            throw new IllegalArgumentException("AppVersionDetails cannot be null");
        }
        if (appVersionDetails.getVersion() == null) {
            throw new IllegalArgumentException("Version cannot be null");
        }
        if (appVersionDetails.getFile() == null) {
            throw new IllegalArgumentException("File content cannot be null");
        }
        if (appVersionDetails.getAppMetaData() == null) {
            throw new IllegalArgumentException("AppMetaData cannot be null");
        }
        if (appVersionDetailsRepository.exists(appVersionDetails.getVersion())) {
            throw new IllegalArgumentException("Version " + appVersionDetails.getVersion() + " already exists");
        }

        String fileUrl = fileService.uploadFile(appVersionDetails.getFile());
        appVersionDetailsRepository.save(appVersionDetails);
        System.out.println("[" + app.getName() + "] Version " + appVersionDetails.getVersion()
                + " uploaded. File URL: " + fileUrl);
    }

    @Override
    public byte[] createUpdatePatch(Version fromVersion, Version toVersion) {
        AppVersionDetails fromDetails = appVersionDetailsRepository.getByVersion(fromVersion);
        AppVersionDetails toDetails = appVersionDetailsRepository.getByVersion(toVersion);

        if (fromDetails == null) {
            throw new IllegalArgumentException("Source version " + fromVersion + " not found");
        }
        if (toDetails == null) {
            throw new IllegalArgumentException("Target version " + toVersion + " not found");
        }
        if (fromVersion.compareTo(toVersion) >= 0) {
            throw new IllegalArgumentException("Source version must be older than target version");
        }

        byte[] diffPack = fileService.createDiffPack(fromDetails.getFile(), toDetails.getFile());
        System.out.println("[" + app.getName() + "] Patch created: " + fromVersion + " -> " + toVersion);
        return diffPack;
    }

    @Override
    public void releaseVersion(AppVersionDetails appVersionDetails) {
        List<Device> eligibleDevices = rolloutStrategy.getEligibleDevices();
        System.out.println("[" + app.getName() + "] Releasing " + appVersionDetails.getVersion()
                + " to " + eligibleDevices.size() + " eligible device(s)");

        for (Device device : eligibleDevices) {
            if (!isHardwareAndOsSupported(device, appVersionDetails)) {
                System.out.println("  Skipping " + device.getId() + " - h/w or OS not supported");
                continue;
            }

            if (device.getAppVersion() == null) {
                doInstall(device, appVersionDetails);
            } else if (device.getAppVersion().compareTo(appVersionDetails.getVersion()) < 0) {
                doUpdate(device, appVersionDetails);
            } else {
                System.out.println("  " + device.getId() + " already on " + device.getAppVersion());
            }
        }
    }

    @Override
    public boolean isAppVersionSupported(Device device, AppVersionDetails appVersionDetails) {
        return isHardwareAndOsSupported(device, appVersionDetails)
                && rolloutStrategy.isDeviceEligible(device);
    }

    @Override
    public boolean checkForInstall(Device device) {
        if (device.getAppVersion() != null) {
            return false;
        }
        AppVersionDetails latest = appVersionDetailsRepository.getLatestVersion();
        if (latest == null) {
            return false;
        }
        return isHardwareAndOsSupported(device, latest);
    }

    @Override
    public boolean checkForUpdates(Device device) {
        if (device.getAppVersion() == null) {
            return false;
        }
        AppVersionDetails latest = appVersionDetailsRepository.getLatestVersion();
        if (latest == null) {
            return false;
        }
        return device.getAppVersion().compareTo(latest.getVersion()) < 0
                && isHardwareAndOsSupported(device, latest);
    }

    @Override
    public void executeTask(Device device, TaskType taskType) {
        AppVersionDetails latest = appVersionDetailsRepository.getLatestVersion();
        if (latest == null) {
            throw new IllegalStateException("No app version available");
        }

        switch (taskType) {
            case INSTALL:
                if (device.getAppVersion() != null) {
                    throw new IllegalStateException("App already installed on device " + device.getId());
                }
                doInstall(device, latest);
                break;
            case UPDATE:
                if (device.getAppVersion() == null) {
                    throw new IllegalStateException("App not installed on device " + device.getId());
                }
                doUpdate(device, latest);
                break;
        }
    }

    private boolean isHardwareAndOsSupported(Device device, AppVersionDetails appVersionDetails) {
        Version minOSVersion;
        if (device.getDeviceType() == DeviceType.IOS) {
            minOSVersion = appVersionDetails.getAppMetaData().getMinimumSupportedIOSVersion();
        } else if (device.getDeviceType() == DeviceType.ANDROID) {
            minOSVersion = appVersionDetails.getAppMetaData().getMinimumSupportedAndroidVersion();
        } else {
            return false;
        }

        if (device.getOsVersion().compareTo(minOSVersion) < 0) {
            return false;
        }

        if (device.getHardwareSpec().getRamInMB() < appVersionDetails.getAppMetaData().getMinimumRamInMB()) {
            return false;
        }

        return device.getHardwareSpec().getStorageInMB() >= appVersionDetails.getAppMetaData().getMinimumStorageInMB();
    }

    private void doInstall(Device device, AppVersionDetails appVersionDetails) {
        fileService.installApp(device.getId(), appVersionDetails.getFile());
        device.setAppVersion(appVersionDetails.getVersion());
        System.out.println("  Installed " + appVersionDetails.getVersion() + " on " + device.getId());
    }

    private void doUpdate(Device device, AppVersionDetails appVersionDetails) {
        AppVersionDetails currentVersion = appVersionDetailsRepository.getByVersion(device.getAppVersion());
        byte[] diffPack;
        if (currentVersion != null) {
            diffPack = fileService.createDiffPack(currentVersion.getFile(), appVersionDetails.getFile());
        } else {
            diffPack = appVersionDetails.getFile();
        }
        fileService.updateApp(device.getId(), diffPack);
        device.setAppVersion(appVersionDetails.getVersion());
        System.out.println("  Updated " + device.getId() + " to " + appVersionDetails.getVersion());
    }
}
