package com.AppVersionManagementSystem.test;

import com.AppVersionManagementSystem.service.impl.InMemoryFileService;
import com.AppVersionManagementSystem.service.interfaces.FileService;

import static com.AppVersionManagementSystem.test.TestRunner.*;

public class FileServiceTest {

    private FileService fileService;

    private void setup() {
        fileService = new InMemoryFileService();
    }

    public void testUploadAndGetFile() {
        setup();
        byte[] content = "test-binary-content".getBytes();
        String url = fileService.uploadFile(content);
        assertNotNull(url, "Upload should return a URL");
        byte[] retrieved = fileService.getFile(url);
        assertEquals(content.length, retrieved.length, "Retrieved file should have same length");
    }

    public void testGetFile_notFound() {
        setup();
        assertThrows(IllegalArgumentException.class,
                () -> fileService.getFile("storage://nonexistent"),
                "Getting non-existent file should throw");
    }

    public void testUploadFile_defensiveCopy() {
        setup();
        byte[] content = "mutable".getBytes();
        String url = fileService.uploadFile(content);
        content[0] = 'X';
        byte[] retrieved = fileService.getFile(url);
        assertFalse(retrieved[0] == 'X', "Modifying original should not affect stored file");
    }

    public void testGetFile_defensiveCopy() {
        setup();
        byte[] content = "immutable-check".getBytes();
        String url = fileService.uploadFile(content);
        byte[] first = fileService.getFile(url);
        first[0] = 'Z';
        byte[] second = fileService.getFile(url);
        assertFalse(second[0] == 'Z', "Modifying retrieved should not affect storage");
    }

    public void testCreateDiffPack_sameLength() {
        setup();
        byte[] source = {1, 2, 3};
        byte[] target = {4, 5, 6};
        byte[] diff = fileService.createDiffPack(source, target);
        assertEquals(3, diff.length, "Diff should have same length as inputs");
        assertEquals((byte) 3, (byte) diff[0], "Diff[0] should be target-source = 3");
    }

    public void testCreateDiffPack_differentLength() {
        setup();
        byte[] source = {1, 2};
        byte[] target = {1, 2, 3, 4};
        byte[] diff = fileService.createDiffPack(source, target);
        assertEquals(4, diff.length, "Diff length should be max of both");
    }

    public void testMultipleUploads_uniqueUrls() {
        setup();
        String url1 = fileService.uploadFile("file1".getBytes());
        String url2 = fileService.uploadFile("file2".getBytes());
        assertFalse(url1.equals(url2), "Each upload should get a unique URL");
    }
}
