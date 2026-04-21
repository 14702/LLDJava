package com.AppVersionManagementSystem.test;

import com.AppVersionManagementSystem.enums.DeviceType;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.AppVersionManagementSystem.test.TestRunner.*;

public class ConcurrencyTest {

    private static final HardwareSpec HW = new HardwareSpec(4096, 64000, "Test");

    public void testConcurrentVersionUploads() throws Exception {
        AppVersionDetailsRepository appRepo = new LocalAppVersionDetailsRepository();
        DeviceRepository deviceRepo = new LocalDeviceRepository();
        FileService fileService = new InMemoryFileService();
        ConcreteAppService appService = new ConcreteAppService(
                new App("TestApp"), appRepo, fileService,
                new BetaRolloutStrategy(deviceRepo, Set.of()));

        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final int ver = i + 1;
            executor.submit(() -> {
                try {
                    appService.uploadNewVersion(new AppVersionDetails(
                            new Version(ver, 0, 0),
                            ("binary-v" + ver).getBytes(),
                            new AppMetaData(new Version(10, 0, 0), new Version(13, 0, 0), 1024, 500)
                    ));
                    successCount.incrementAndGet();
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(3, TimeUnit.SECONDS);
        executor.shutdown();
        assertEquals(threadCount, successCount.get(), "All concurrent uploads should succeed");
        assertEquals(new Version(threadCount, 0, 0), appRepo.getLatestVersion().getVersion(),
                "Latest version should be the highest");
    }

    public void testConcurrentDeviceRegistration() throws Exception {
        DeviceRepository deviceRepo = new LocalDeviceRepository();
        int threadCount = 20;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int id = i;
            executor.submit(() -> {
                try {
                    deviceRepo.addDevice(new Device(
                            "device-" + id, id % 2 == 0, null,
                            id % 2 == 0 ? DeviceType.IOS : DeviceType.ANDROID,
                            new Version(12, 0, 0), HW));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(3, TimeUnit.SECONDS);
        executor.shutdown();
        assertEquals(threadCount, deviceRepo.getAllDevices().size(),
                "All devices should be registered");
    }

    public void testConcurrentFileUploadAndRetrieve() throws Exception {
        FileService fileService = new InMemoryFileService();
        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<String> urls = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            final int idx = i;
            executor.submit(() -> {
                try {
                    String url = fileService.uploadFile(("content-" + idx).getBytes());
                    synchronized (urls) {
                        urls.add(url);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(3, TimeUnit.SECONDS);
        executor.shutdown();
        assertEquals(threadCount, urls.size(), "All uploads should succeed");
        for (String url : urls) {
            assertNotNull(fileService.getFile(url), "Each file should be retrievable");
        }
    }
}
