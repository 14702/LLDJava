package com.AppVersionManagementSystem.service.impl;

import com.AppVersionManagementSystem.service.interfaces.FileService;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryFileService implements FileService {

    private final ConcurrentHashMap<String, byte[]> fileStorage = new ConcurrentHashMap<>();
    private final AtomicLong fileIdCounter = new AtomicLong(0);

    @Override
    public void installApp(String deviceId, byte[] appFile) {
        System.out.println("App installed on device " + deviceId);
    }

    @Override
    public void updateApp(String deviceId, byte[] diffPack) {
        System.out.println("App updated on device " + deviceId + " with diff pack of size " + diffPack.length);
    }

    @Override
    public byte[] createDiffPack(byte[] sourceFile, byte[] targetFile) {
        int len = Math.max(sourceFile.length, targetFile.length);
        byte[] diff = new byte[len];
        for (int i = 0; i < len; i++) {
            byte s = i < sourceFile.length ? sourceFile[i] : 0;
            byte t = i < targetFile.length ? targetFile[i] : 0;
            diff[i] = (byte) (t - s);
        }
        System.out.println("Diff pack created, size: " + diff.length + " bytes");
        return diff;
    }

    @Override
    public String uploadFile(byte[] fileContent) {
        String url = "storage://file-" + fileIdCounter.incrementAndGet();
        fileStorage.put(url, Arrays.copyOf(fileContent, fileContent.length));
        System.out.println("File uploaded to " + url);
        return url;
    }

    @Override
    public byte[] getFile(String fileUrl) {
        byte[] content = fileStorage.get(fileUrl);
        if (content == null) {
            throw new IllegalArgumentException("File not found at " + fileUrl);
        }
        return Arrays.copyOf(content, content.length);
    }
}
