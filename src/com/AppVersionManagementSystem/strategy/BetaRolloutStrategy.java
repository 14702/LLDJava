package com.AppVersionManagementSystem.strategy;

import com.AppVersionManagementSystem.model.AppVersionDetails;
import com.AppVersionManagementSystem.repository.DeviceRepository;
import com.AppVersionManagementSystem.utility.AppUtility;

public class BetaRolloutStrategy implements RolloutStrategy {

    private final DeviceRepository deviceRepository;

    public BetaRolloutStrategy(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public void rollout(AppVersionDetails appVersionDetails) {
        System.out.println(deviceRepository.getAllDevices());
        deviceRepository
                .getAllDevices()
                .stream()
                .filter(d -> d.isBetaVersionEnabled())
                .forEach( d -> {
                    String diff = AppUtility.createDiffPack(d.getAppVersion(), appVersionDetails.getVersion());
                    deviceRepository.update(d, appVersionDetails, diff);
                });
    }
}