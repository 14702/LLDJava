package com.AppVersionManagementSystem.test;

import com.AppVersionManagementSystem.enums.DeviceType;
import com.AppVersionManagementSystem.model.Device;
import com.AppVersionManagementSystem.model.HardwareSpec;
import com.AppVersionManagementSystem.model.Version;
import com.AppVersionManagementSystem.repository.impl.LocalDeviceRepository;
import com.AppVersionManagementSystem.repository.interfaces.DeviceRepository;

import static com.AppVersionManagementSystem.test.TestRunner.*;

public class DeviceRepositoryTest {

    private DeviceRepository repo;
    private static final HardwareSpec DEFAULT_HW = new HardwareSpec(4096, 64000, "TestDevice");

    private void setup() {
        repo = new LocalDeviceRepository();
    }

    public void testAddAndGetDevice() {
        setup();
        Device device = new Device("d1", false, null, DeviceType.ANDROID, new Version(12, 0, 0), DEFAULT_HW);
        repo.addDevice(device);
        Device result = repo.getById("d1");
        assertNotNull(result, "Added device should be retrievable");
        assertEquals("d1", result.getId(), "Device ID should match");
    }

    public void testExists_true() {
        setup();
        repo.addDevice(new Device("d1", false, null, DeviceType.IOS, new Version(15, 0, 0), DEFAULT_HW));
        assertTrue(repo.exists("d1"), "Added device should exist");
    }

    public void testExists_false() {
        setup();
        assertFalse(repo.exists("nonexistent"), "Non-added device should not exist");
    }

    public void testGetAllDevices() {
        setup();
        repo.addDevice(new Device("d1", false, null, DeviceType.ANDROID, new Version(12, 0, 0), DEFAULT_HW));
        repo.addDevice(new Device("d2", true, null, DeviceType.IOS, new Version(15, 0, 0), DEFAULT_HW));
        assertEquals(2, repo.getAllDevices().size(), "Should return all added devices");
    }

    public void testGetById_nonExistent() {
        setup();
        assertNull(repo.getById("ghost"), "Non-existent device should return null");
    }

    public void testGetAllDevices_empty() {
        setup();
        assertEquals(0, repo.getAllDevices().size(), "Empty repo should return empty list");
    }

    public void testGetAllDevices_returnsDefensiveCopy() {
        setup();
        repo.addDevice(new Device("d1", false, null, DeviceType.ANDROID, new Version(12, 0, 0), DEFAULT_HW));
        repo.getAllDevices().clear();
        assertEquals(1, repo.getAllDevices().size(),
                "Clearing returned list should not affect internal state");
    }
}
