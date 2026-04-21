package com.AppVersionManagementSystem;

import com.AppVersionManagementSystem.enums.DeviceType;
import com.AppVersionManagementSystem.enums.TaskType;
import com.AppVersionManagementSystem.model.App;
import com.AppVersionManagementSystem.model.AppMetaData;
import com.AppVersionManagementSystem.model.AppVersionDetails;
import com.AppVersionManagementSystem.model.Device;
import com.AppVersionManagementSystem.model.HardwareSpec;
import com.AppVersionManagementSystem.model.Version;
import com.AppVersionManagementSystem.repository.impl.LocalAppVersionDetailsRepository;
import com.AppVersionManagementSystem.repository.impl.LocalDeviceRepository;
import com.AppVersionManagementSystem.repository.interfaces.AppVersionDetailsRepository;
import com.AppVersionManagementSystem.repository.interfaces.DeviceRepository;
import com.AppVersionManagementSystem.service.impl.ConcreteAppService;
import com.AppVersionManagementSystem.service.impl.InMemoryFileService;
import com.AppVersionManagementSystem.service.interfaces.AppService;
import com.AppVersionManagementSystem.service.interfaces.FileService;
import com.AppVersionManagementSystem.strategy.impl.BetaRolloutStrategy;
import com.AppVersionManagementSystem.strategy.impl.PercentageRolloutStrategy;

import java.util.Set;

public class Main {
    public static void main(String[] args) {
        App phonePe = new App("PhonePe");

        AppVersionDetailsRepository appVersionRepo = new LocalAppVersionDetailsRepository();
        DeviceRepository deviceRepo = new LocalDeviceRepository();
        FileService fileService = new InMemoryFileService();

        HardwareSpec goodHw = new HardwareSpec(4096, 64000, "Samsung Galaxy S21");
        HardwareSpec lowHw = new HardwareSpec(512, 4000, "Budget Phone");

        Device iosDevice = new Device("ios-001", true, null, DeviceType.IOS,
                new Version(15, 0, 0), goodHw);
        Device androidDevice = new Device("android-001", false, null, DeviceType.ANDROID,
                new Version(12, 0, 0), goodHw);
        Device oldAndroid = new Device("android-002", true, null, DeviceType.ANDROID,
                new Version(8, 0, 0), lowHw);

        deviceRepo.addDevice(iosDevice);
        deviceRepo.addDevice(androidDevice);
        deviceRepo.addDevice(oldAndroid);

        // v1: min OS 10, min RAM 1024MB, min storage 500MB
        AppVersionDetails v1 = new AppVersionDetails(
                new Version(1, 0, 0),
                "phonepe-v1-binary".getBytes(),
                new AppMetaData(new Version(10, 0, 0), new Version(13, 0, 0), 1024, 500)
        );

        // --- Beta rollout ---
        BetaRolloutStrategy betaStrategy = new BetaRolloutStrategy(deviceRepo, Set.of());
        AppService betaService = new ConcreteAppService(phonePe, appVersionRepo, fileService, betaStrategy);

        System.out.println("=== Upload v1.0.0 ===");
        betaService.uploadNewVersion(v1);

        System.out.println("\n=== Check Install Eligibility ===");
        System.out.println("ios-001 can install: " + betaService.checkForInstall(iosDevice));
        System.out.println("android-001 can install: " + betaService.checkForInstall(androidDevice));
        System.out.println("android-002 (old OS, low h/w): " + betaService.checkForInstall(oldAndroid));

        System.out.println("\n=== Execute Install ===");
        betaService.executeTask(iosDevice, TaskType.INSTALL);
        betaService.executeTask(androidDevice, TaskType.INSTALL);

        // v2
        AppVersionDetails v2 = new AppVersionDetails(
                new Version(2, 0, 0),
                "phonepe-v2-dark-mode".getBytes(),
                new AppMetaData(new Version(10, 0, 0), new Version(13, 0, 0), 1024, 500)
        );

        System.out.println("\n=== Upload v2.0.0 ===");
        betaService.uploadNewVersion(v2);

        System.out.println("\n=== Create Update Patch (v1 -> v2) ===");
        byte[] patch = betaService.createUpdatePatch(new Version(1, 0, 0), new Version(2, 0, 0));
        System.out.println("Patch size: " + patch.length + " bytes");

        System.out.println("\n=== Check For Updates ===");
        System.out.println("ios-001 has update: " + betaService.checkForUpdates(iosDevice));
        System.out.println("android-001 has update: " + betaService.checkForUpdates(androidDevice));

        System.out.println("\n=== Beta Rollout v2.0.0 ===");
        betaService.releaseVersion(v2);

        System.out.println("\n=== isAppVersionSupported (includes rollout strategy check) ===");
        System.out.println("ios-001 (beta): " + betaService.isAppVersionSupported(iosDevice, v2));
        System.out.println("android-001 (non-beta): " + betaService.isAppVersionSupported(androidDevice, v2));
        System.out.println("android-002 (beta but old OS): " + betaService.isAppVersionSupported(oldAndroid, v2));

        // --- Switch to percentage rollout for v3 ---
        AppVersionDetails v3 = new AppVersionDetails(
                new Version(3, 0, 0),
                "phonepe-v3-upi-lite".getBytes(),
                new AppMetaData(new Version(10, 0, 0), new Version(13, 0, 0), 1024, 500)
        );

        PercentageRolloutStrategy percentStrategy = new PercentageRolloutStrategy(deviceRepo, 50);
        AppService percentService = new ConcreteAppService(phonePe, appVersionRepo, fileService, percentStrategy);

        System.out.println("\n=== Upload v3.0.0 ===");
        percentService.uploadNewVersion(v3);

        System.out.println("\n=== Percentage Rollout v3.0.0 (50%) ===");
        percentService.releaseVersion(v3);
    }
}
