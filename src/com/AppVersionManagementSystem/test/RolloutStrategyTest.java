package com.AppVersionManagementSystem.test;

import com.AppVersionManagementSystem.enums.DeviceType;
import com.AppVersionManagementSystem.model.Device;
import com.AppVersionManagementSystem.model.HardwareSpec;
import com.AppVersionManagementSystem.model.Version;
import com.AppVersionManagementSystem.repository.impl.LocalDeviceRepository;
import com.AppVersionManagementSystem.repository.interfaces.DeviceRepository;
import com.AppVersionManagementSystem.strategy.impl.BetaRolloutStrategy;
import com.AppVersionManagementSystem.strategy.impl.PercentageRolloutStrategy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.AppVersionManagementSystem.test.TestRunner.*;

public class RolloutStrategyTest {

    private DeviceRepository deviceRepo;
    private static final HardwareSpec HW = new HardwareSpec(4096, 64000, "Test");

    private void setup() {
        deviceRepo = new LocalDeviceRepository();
    }

    private void addDevices() {
        deviceRepo.addDevice(new Device("beta-1", true, null, DeviceType.ANDROID, new Version(12, 0, 0), HW));
        deviceRepo.addDevice(new Device("beta-2", true, null, DeviceType.IOS, new Version(15, 0, 0), HW));
        deviceRepo.addDevice(new Device("normal-1", false, null, DeviceType.ANDROID, new Version(12, 0, 0), HW));
        deviceRepo.addDevice(new Device("normal-2", false, null, DeviceType.IOS, new Version(15, 0, 0), HW));
    }

    // --- BetaRolloutStrategy ---

    public void testBetaStrategy_onlyBetaDevices() {
        setup();
        addDevices();
        BetaRolloutStrategy s = new BetaRolloutStrategy(deviceRepo, Set.of());
        List<Device> eligible = s.getEligibleDevices();
        assertEquals(2, eligible.size(), "Only beta-enabled devices should be eligible");
        assertTrue(eligible.stream().allMatch(Device::isBetaEnabled), "All must have beta enabled");
    }

    public void testBetaStrategy_isDeviceEligible() {
        setup();
        addDevices();
        BetaRolloutStrategy s = new BetaRolloutStrategy(deviceRepo, Set.of());
        assertTrue(s.isDeviceEligible(deviceRepo.getById("beta-1")), "Beta device should be eligible");
        assertFalse(s.isDeviceEligible(deviceRepo.getById("normal-1")), "Non-beta should not be eligible");
    }

    public void testBetaStrategy_withExplicitDeviceIds() {
        setup();
        addDevices();
        BetaRolloutStrategy s = new BetaRolloutStrategy(deviceRepo, Set.of("normal-1"));
        List<Device> eligible = s.getEligibleDevices();
        assertEquals(3, eligible.size(), "Beta + explicit device should be eligible");
        assertTrue(s.isDeviceEligible(deviceRepo.getById("normal-1")),
                "Explicitly added device should be eligible");
    }

    public void testBetaStrategy_noBetaDevices() {
        setup();
        deviceRepo.addDevice(new Device("n1", false, null, DeviceType.ANDROID, new Version(12, 0, 0), HW));
        BetaRolloutStrategy s = new BetaRolloutStrategy(deviceRepo, Set.of());
        assertEquals(0, s.getEligibleDevices().size(), "No beta = no eligible");
    }

    public void testBetaStrategy_emptyRepo() {
        setup();
        BetaRolloutStrategy s = new BetaRolloutStrategy(deviceRepo, Set.of());
        assertEquals(0, s.getEligibleDevices().size(), "Empty repo = empty list");
    }

    public void testBetaStrategy_immutableDeviceIds() {
        setup();
        addDevices();
        Set<String> mutableSet = new HashSet<>();
        mutableSet.add("normal-1");
        BetaRolloutStrategy s = new BetaRolloutStrategy(deviceRepo, mutableSet);
        mutableSet.add("normal-2");
        assertEquals(3, s.getEligibleDevices().size(),
                "Modifying original set should not affect strategy");
    }

    // --- PercentageRolloutStrategy ---

    public void testPercentageStrategy_100Percent() {
        setup();
        addDevices();
        PercentageRolloutStrategy s = new PercentageRolloutStrategy(deviceRepo, 100);
        assertEquals(4, s.getEligibleDevices().size(), "100% should include all");
    }

    public void testPercentageStrategy_0Percent() {
        setup();
        addDevices();
        PercentageRolloutStrategy s = new PercentageRolloutStrategy(deviceRepo, 0);
        assertEquals(0, s.getEligibleDevices().size(), "0% should include none");
    }

    public void testPercentageStrategy_50Percent() {
        setup();
        addDevices();
        PercentageRolloutStrategy s = new PercentageRolloutStrategy(deviceRepo, 50);
        assertEquals(2, s.getEligibleDevices().size(), "50% of 4 = 2");
    }

    public void testPercentageStrategy_isDeviceEligible_consistent() {
        setup();
        addDevices();
        PercentageRolloutStrategy s = new PercentageRolloutStrategy(deviceRepo, 50);
        List<Device> eligible = s.getEligibleDevices();
        for (Device d : eligible) {
            assertTrue(s.isDeviceEligible(d), "Device in eligible list must pass isDeviceEligible");
        }
    }

    public void testPercentageStrategy_invalidPercentage() {
        setup();
        assertThrows(IllegalArgumentException.class,
                () -> new PercentageRolloutStrategy(deviceRepo, 101), "> 100 should throw");
        assertThrows(IllegalArgumentException.class,
                () -> new PercentageRolloutStrategy(deviceRepo, -1), "< 0 should throw");
    }

    public void testPercentageStrategy_emptyRepo() {
        setup();
        PercentageRolloutStrategy s = new PercentageRolloutStrategy(deviceRepo, 50);
        assertEquals(0, s.getEligibleDevices().size(), "Empty repo = empty list");
    }
}
