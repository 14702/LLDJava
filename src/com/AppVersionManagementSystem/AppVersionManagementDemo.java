package com.AppVersionManagementSystem;

import com.AppVersionManagementSystem.enums.DeviceType;
import com.AppVersionManagementSystem.model.AppMetaData;
import com.AppVersionManagementSystem.model.AppVersionDetails;
import com.AppVersionManagementSystem.model.Device;
import com.AppVersionManagementSystem.model.Version;
import com.AppVersionManagementSystem.repository.AppVersionDetailsRepository;
import com.AppVersionManagementSystem.repository.DeviceRepository;
import com.AppVersionManagementSystem.repository.LocalAppVersionDetailsRepository;
import com.AppVersionManagementSystem.repository.LocalDeviceRepository;
import com.AppVersionManagementSystem.strategy.BetaRolloutStrategy;
import com.AppVersionManagementSystem.strategy.RolloutStrategy;
import com.AppVersionManagementSystem.service.AppService;
import com.AppVersionManagementSystem.service.ConcreteAppService;

import java.util.UUID;

public class AppVersionManagementDemo {
    public static void main(String[] args) {
        AppVersionDetailsRepository appVersionDetailsRepository = new LocalAppVersionDetailsRepository();
        DeviceRepository deviceRepository = new LocalDeviceRepository();

        RolloutStrategy betaRolloutStrategy = new BetaRolloutStrategy(deviceRepository);

        Device device1 = new Device(
                "device1",
                false,
                null,
                DeviceType.IOS,
                new Version(10, 0, 0)
        );

        Device device2 = new Device(
                "device2",
                true,
                null,
                DeviceType.ANDROID,
                new Version(10, 0, 0)
        );

        deviceRepository.addDevice(device1);
        deviceRepository.addDevice(device2);

        AppService appService = new ConcreteAppService(appVersionDetailsRepository, betaRolloutStrategy, deviceRepository);
        AppVersionDetails appVersionDetails = new AppVersionDetails(
                new Version(1, 0, 0),
                UUID.randomUUID().toString().getBytes(),
                new AppMetaData(
                        new Version(3, 0, 0),
                        new Version(3, 0, 0)
                )
        );

        AppVersionDetails appVersionDetails2 = new AppVersionDetails(
                new Version(2, 0, 0),
                UUID.randomUUID().toString().getBytes(),
                new AppMetaData(
                        new Version(3, 0, 0),
                        new Version(3, 0, 0)
                )
        );

        appService.uploadNewVersion(appVersionDetails);
        System.out.println(appService.checkForInstall(device1, appVersionDetails));
        System.out.println(appService.checkForInstall(device2, appVersionDetails));

        appService.releaseVersion(appVersionDetails);

        String diff = appService.createUpdatePatch(appVersionDetails, appVersionDetails2);

        appService.uploadNewVersion(appVersionDetails2);
        System.out.println(appService.checkForUpdates(device1, appVersionDetails2));
        System.out.println(appService.checkForUpdates(device2, appVersionDetails2));
        appService.releaseVersion(appVersionDetails2);
    }
}