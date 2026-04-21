package com.AppVersionManagementSystem.test;

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
import com.AppVersionManagementSystem.service.interfaces.FileService;
import com.AppVersionManagementSystem.strategy.impl.BetaRolloutStrategy;
import com.AppVersionManagementSystem.strategy.interfaces.RolloutStrategy;

import java.util.Set;

import static com.AppVersionManagementSystem.test.TestRunner.*;

public class AppServiceTest {

    private static final HardwareSpec GOOD_HW = new HardwareSpec(4096, 64000, "TestDevice");
    private static final HardwareSpec LOW_RAM_HW = new HardwareSpec(256, 64000, "LowRam");
    private static final HardwareSpec LOW_STORAGE_HW = new HardwareSpec(4096, 100, "LowStorage");

    private ConcreteAppService appService;
    private AppVersionDetailsRepository appRepo;
    private DeviceRepository deviceRepo;

    private void setup() {
        appRepo = new LocalAppVersionDetailsRepository();
        deviceRepo = new LocalDeviceRepository();
        FileService fileService = new InMemoryFileService();
        RolloutStrategy strategy = new BetaRolloutStrategy(deviceRepo, Set.of());
        appService = new ConcreteAppService(new App("TestApp"), appRepo, fileService, strategy);
    }

    private AppVersionDetails ver(int major, int minor, int patch, int minAndroid, int minIos) {
        return new AppVersionDetails(
                new Version(major, minor, patch),
                ("binary-v" + major + "." + minor + "." + patch).getBytes(),
                new AppMetaData(new Version(minAndroid, 0, 0), new Version(minIos, 0, 0), 1024, 500)
        );
    }

    // --- uploadNewVersion ---

    public void testUploadNewVersion_success() {
        setup();
        appService.uploadNewVersion(ver(1, 0, 0, 10, 13));
        assertTrue(appRepo.exists(new Version(1, 0, 0)), "Version should be stored after upload");
    }

    public void testUploadNewVersion_duplicateThrows() {
        setup();
        appService.uploadNewVersion(ver(1, 0, 0, 10, 13));
        assertThrows(IllegalArgumentException.class,
                () -> appService.uploadNewVersion(ver(1, 0, 0, 10, 13)),
                "Uploading duplicate version should throw");
    }

    public void testUploadNewVersion_nullThrows() {
        setup();
        assertThrows(IllegalArgumentException.class,
                () -> appService.uploadNewVersion(null),
                "Uploading null should throw");
    }

    // --- isAppVersionSupported (checks OS + h/w + rollout strategy) ---

    public void testIsAppVersionSupported_androidSupported_betaDevice() {
        setup();
        Device d = new Device("a1", true, null, DeviceType.ANDROID, new Version(12, 0, 0), GOOD_HW);
        deviceRepo.addDevice(d);
        assertTrue(appService.isAppVersionSupported(d, ver(1, 0, 0, 10, 13)),
                "Beta Android 12 with good h/w should be supported");
    }

    public void testIsAppVersionSupported_androidNotSupported_oldOS() {
        setup();
        Device d = new Device("a2", true, null, DeviceType.ANDROID, new Version(8, 0, 0), GOOD_HW);
        deviceRepo.addDevice(d);
        assertFalse(appService.isAppVersionSupported(d, ver(1, 0, 0, 10, 13)),
                "Android 8 should NOT support app requiring Android 10+");
    }

    public void testIsAppVersionSupported_notEligibleByStrategy() {
        setup();
        Device nonBeta = new Device("nb", false, null, DeviceType.ANDROID, new Version(12, 0, 0), GOOD_HW);
        deviceRepo.addDevice(nonBeta);
        assertFalse(appService.isAppVersionSupported(nonBeta, ver(1, 0, 0, 10, 13)),
                "Non-beta device should fail strategy eligibility check");
    }

    public void testIsAppVersionSupported_iosSupported() {
        setup();
        Device d = new Device("i1", true, null, DeviceType.IOS, new Version(15, 0, 0), GOOD_HW);
        deviceRepo.addDevice(d);
        assertTrue(appService.isAppVersionSupported(d, ver(1, 0, 0, 10, 13)),
                "iOS 15 beta device should be supported");
    }

    public void testIsAppVersionSupported_iosNotSupported() {
        setup();
        Device d = new Device("i2", true, null, DeviceType.IOS, new Version(12, 0, 0), GOOD_HW);
        deviceRepo.addDevice(d);
        assertFalse(appService.isAppVersionSupported(d, ver(1, 0, 0, 10, 13)),
                "iOS 12 should NOT support app requiring iOS 13+");
    }

    public void testIsAppVersionSupported_lowRam() {
        setup();
        Device d = new Device("lr", true, null, DeviceType.ANDROID, new Version(12, 0, 0), LOW_RAM_HW);
        deviceRepo.addDevice(d);
        assertFalse(appService.isAppVersionSupported(d, ver(1, 0, 0, 10, 13)),
                "Device with 256MB RAM should not support app requiring 1024MB");
    }

    public void testIsAppVersionSupported_lowStorage() {
        setup();
        Device d = new Device("ls", true, null, DeviceType.ANDROID, new Version(12, 0, 0), LOW_STORAGE_HW);
        deviceRepo.addDevice(d);
        assertFalse(appService.isAppVersionSupported(d, ver(1, 0, 0, 10, 13)),
                "Device with 100MB storage should not support app requiring 500MB");
    }

    public void testIsAppVersionSupported_exactMinimumVersion() {
        setup();
        Device d = new Device("d1", true, null, DeviceType.ANDROID, new Version(10, 0, 0), GOOD_HW);
        deviceRepo.addDevice(d);
        assertTrue(appService.isAppVersionSupported(d, ver(1, 0, 0, 10, 13)),
                "Device with exact minimum OS should be supported");
    }

    // --- checkForInstall ---

    public void testCheckForInstall_noAppInstalled_supported() {
        setup();
        Device d = new Device("d1", false, null, DeviceType.ANDROID, new Version(12, 0, 0), GOOD_HW);
        appService.uploadNewVersion(ver(1, 0, 0, 10, 13));
        assertTrue(appService.checkForInstall(d), "Device without app should be eligible for install");
    }

    public void testCheckForInstall_appAlreadyInstalled() {
        setup();
        Device d = new Device("d1", false, new Version(1, 0, 0), DeviceType.ANDROID, new Version(12, 0, 0), GOOD_HW);
        appService.uploadNewVersion(ver(1, 0, 0, 10, 13));
        assertFalse(appService.checkForInstall(d), "Device with app should not be eligible");
    }

    public void testCheckForInstall_osNotSupported() {
        setup();
        Device d = new Device("d1", false, null, DeviceType.ANDROID, new Version(8, 0, 0), GOOD_HW);
        appService.uploadNewVersion(ver(1, 0, 0, 10, 13));
        assertFalse(appService.checkForInstall(d), "Unsupported OS should not be eligible");
    }

    public void testCheckForInstall_noVersionsUploaded() {
        setup();
        Device d = new Device("d1", false, null, DeviceType.ANDROID, new Version(12, 0, 0), GOOD_HW);
        assertFalse(appService.checkForInstall(d), "No versions uploaded, should return false");
    }

    public void testCheckForInstall_hardwareNotSupported() {
        setup();
        Device d = new Device("d1", false, null, DeviceType.ANDROID, new Version(12, 0, 0), LOW_RAM_HW);
        appService.uploadNewVersion(ver(1, 0, 0, 10, 13));
        assertFalse(appService.checkForInstall(d), "Low RAM device should not be eligible");
    }

    // --- checkForUpdates ---

    public void testCheckForUpdates_updateAvailable() {
        setup();
        Device d = new Device("d1", false, new Version(1, 0, 0), DeviceType.ANDROID, new Version(12, 0, 0), GOOD_HW);
        appService.uploadNewVersion(ver(1, 0, 0, 10, 13));
        appService.uploadNewVersion(ver(2, 0, 0, 10, 13));
        assertTrue(appService.checkForUpdates(d), "Device on v1 should have update to v2");
    }

    public void testCheckForUpdates_alreadyLatest() {
        setup();
        Device d = new Device("d1", false, new Version(1, 0, 0), DeviceType.ANDROID, new Version(12, 0, 0), GOOD_HW);
        appService.uploadNewVersion(ver(1, 0, 0, 10, 13));
        assertFalse(appService.checkForUpdates(d), "Already on latest, no update");
    }

    public void testCheckForUpdates_noAppInstalled() {
        setup();
        Device d = new Device("d1", false, null, DeviceType.ANDROID, new Version(12, 0, 0), GOOD_HW);
        appService.uploadNewVersion(ver(1, 0, 0, 10, 13));
        assertFalse(appService.checkForUpdates(d), "No app installed, cannot update");
    }

    public void testCheckForUpdates_osNotSupportedForNewVersion() {
        setup();
        Device d = new Device("d1", false, new Version(1, 0, 0), DeviceType.ANDROID, new Version(9, 0, 0), GOOD_HW);
        appService.uploadNewVersion(ver(1, 0, 0, 8, 8, 512, 200));
        appService.uploadNewVersion(ver(2, 0, 0, 10, 13));
        assertFalse(appService.checkForUpdates(d), "OS doesn't support new version");
    }

    // helper with custom h/w requirements
    private AppVersionDetails ver(int major, int minor, int patch, int minAndroid, int minIos,
                                  int minRam, int minStorage) {
        return new AppVersionDetails(
                new Version(major, minor, patch),
                ("binary-v" + major + "." + minor + "." + patch).getBytes(),
                new AppMetaData(new Version(minAndroid, 0, 0), new Version(minIos, 0, 0), minRam, minStorage)
        );
    }

    // --- createUpdatePatch ---

    public void testCreateUpdatePatch_success() {
        setup();
        appService.uploadNewVersion(ver(1, 0, 0, 10, 13));
        appService.uploadNewVersion(ver(2, 0, 0, 10, 13));
        byte[] patch = appService.createUpdatePatch(new Version(1, 0, 0), new Version(2, 0, 0));
        assertNotNull(patch, "Patch should not be null");
        assertTrue(patch.length > 0, "Patch should have content");
    }

    public void testCreateUpdatePatch_sourceNotFound() {
        setup();
        appService.uploadNewVersion(ver(2, 0, 0, 10, 13));
        assertThrows(IllegalArgumentException.class,
                () -> appService.createUpdatePatch(new Version(1, 0, 0), new Version(2, 0, 0)),
                "Should throw when source not found");
    }

    public void testCreateUpdatePatch_targetNotFound() {
        setup();
        appService.uploadNewVersion(ver(1, 0, 0, 10, 13));
        assertThrows(IllegalArgumentException.class,
                () -> appService.createUpdatePatch(new Version(1, 0, 0), new Version(2, 0, 0)),
                "Should throw when target not found");
    }

    public void testCreateUpdatePatch_sourceNewerThanTarget() {
        setup();
        appService.uploadNewVersion(ver(1, 0, 0, 10, 13));
        appService.uploadNewVersion(ver(2, 0, 0, 10, 13));
        assertThrows(IllegalArgumentException.class,
                () -> appService.createUpdatePatch(new Version(2, 0, 0), new Version(1, 0, 0)),
                "Should throw when source newer than target");
    }

    // --- executeTask ---

    public void testExecuteTask_install() {
        setup();
        Device d = new Device("d1", false, null, DeviceType.ANDROID, new Version(12, 0, 0), GOOD_HW);
        appService.uploadNewVersion(ver(1, 0, 0, 10, 13));
        appService.executeTask(d, TaskType.INSTALL);
        assertEquals(new Version(1, 0, 0), d.getAppVersion(), "After install, should have v1");
    }

    public void testExecuteTask_update() {
        setup();
        appService.uploadNewVersion(ver(1, 0, 0, 10, 13));
        appService.uploadNewVersion(ver(2, 0, 0, 10, 13));
        Device d = new Device("d1", false, new Version(1, 0, 0), DeviceType.ANDROID, new Version(12, 0, 0), GOOD_HW);
        appService.executeTask(d, TaskType.UPDATE);
        assertEquals(new Version(2, 0, 0), d.getAppVersion(), "After update, should have v2");
    }

    public void testExecuteTask_installWhenAlreadyInstalled() {
        setup();
        Device d = new Device("d1", false, new Version(1, 0, 0), DeviceType.ANDROID, new Version(12, 0, 0), GOOD_HW);
        appService.uploadNewVersion(ver(1, 0, 0, 10, 13));
        assertThrows(IllegalStateException.class,
                () -> appService.executeTask(d, TaskType.INSTALL),
                "Install should fail if app already installed");
    }

    public void testExecuteTask_updateWhenNotInstalled() {
        setup();
        Device d = new Device("d1", false, null, DeviceType.ANDROID, new Version(12, 0, 0), GOOD_HW);
        appService.uploadNewVersion(ver(1, 0, 0, 10, 13));
        assertThrows(IllegalStateException.class,
                () -> appService.executeTask(d, TaskType.UPDATE),
                "Update should fail if app not installed");
    }
}
