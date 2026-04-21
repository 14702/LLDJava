package com.AppVersionManagementSystem.test;

import com.AppVersionManagementSystem.model.AppMetaData;
import com.AppVersionManagementSystem.model.AppVersionDetails;
import com.AppVersionManagementSystem.model.Version;
import com.AppVersionManagementSystem.repository.impl.LocalAppVersionDetailsRepository;
import com.AppVersionManagementSystem.repository.interfaces.AppVersionDetailsRepository;

import static com.AppVersionManagementSystem.test.TestRunner.*;

public class AppVersionDetailsRepositoryTest {

    private AppVersionDetailsRepository repo;

    private void setup() {
        repo = new LocalAppVersionDetailsRepository();
    }

    private AppVersionDetails v(int major, int minor, int patch) {
        return new AppVersionDetails(
                new Version(major, minor, patch),
                ("file-v" + major + "." + minor + "." + patch).getBytes(),
                new AppMetaData(new Version(10, 0, 0), new Version(13, 0, 0), 1024, 500)
        );
    }

    public void testSave_andRetrieveByVersion() {
        setup();
        AppVersionDetails v1 = v(1, 0, 0);
        repo.save(v1);
        AppVersionDetails result = repo.getByVersion(new Version(1, 0, 0));
        assertNotNull(result, "Saved version should be retrievable");
        assertEquals(v1.getVersion(), result.getVersion(), "Retrieved version should match");
    }

    public void testGetLatestVersion_singleVersion() {
        setup();
        AppVersionDetails v1 = v(1, 0, 0);
        repo.save(v1);
        assertEquals(v1, repo.getLatestVersion(), "Latest should be the only saved version");
    }

    public void testGetLatestVersion_multipleVersions() {
        setup();
        repo.save(v(1, 0, 0));
        repo.save(v(3, 0, 0));
        repo.save(v(2, 0, 0));
        assertEquals(new Version(3, 0, 0), repo.getLatestVersion().getVersion(),
                "Latest should be 3.0.0 regardless of insertion order");
    }

    public void testExists_true() {
        setup();
        repo.save(v(1, 0, 0));
        assertTrue(repo.exists(new Version(1, 0, 0)), "Saved version should exist");
    }

    public void testExists_false() {
        setup();
        assertFalse(repo.exists(new Version(9, 9, 9)), "Non-saved version should not exist");
    }

    public void testGetByVersion_nonExistent() {
        setup();
        assertNull(repo.getByVersion(new Version(1, 0, 0)), "Non-existent should return null");
    }

    public void testGetLatestVersion_empty() {
        setup();
        assertNull(repo.getLatestVersion(), "Empty repo latest should be null");
    }
}
