package com.AppVersionManagementSystem.service.interfaces;

public interface FileService {
    void installApp(String deviceId, byte[] appFile);
    void updateApp(String deviceId, byte[] diffPack);
    byte[] createDiffPack(byte[] sourceFile, byte[] targetFile);
    String uploadFile(byte[] fileContent);
    byte[] getFile(String fileUrl);
}
