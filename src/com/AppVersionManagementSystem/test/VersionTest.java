package com.AppVersionManagementSystem.test;

import com.AppVersionManagementSystem.model.Version;

import static com.AppVersionManagementSystem.test.TestRunner.*;

public class VersionTest {

    public void testEquals_sameVersion() {
        Version v1 = new Version(1, 0, 0);
        Version v2 = new Version(1, 0, 0);
        assertTrue(v1.equals(v2), "Same versions should be equal");
    }

    public void testEquals_differentVersion() {
        Version v1 = new Version(1, 0, 0);
        Version v2 = new Version(2, 0, 0);
        assertFalse(v1.equals(v2), "Different versions should not be equal");
    }

    public void testHashCode_sameVersion() {
        Version v1 = new Version(1, 2, 3);
        Version v2 = new Version(1, 2, 3);
        assertEquals(v1.hashCode(), v2.hashCode(), "Equal versions must have same hashCode");
    }

    public void testCompareTo_greaterMajor() {
        Version v1 = new Version(2, 0, 0);
        Version v2 = new Version(1, 9, 9);
        assertTrue(v1.compareTo(v2) > 0, "2.0.0 should be greater than 1.9.9");
    }

    public void testCompareTo_greaterMinor() {
        Version v1 = new Version(1, 2, 0);
        Version v2 = new Version(1, 1, 9);
        assertTrue(v1.compareTo(v2) > 0, "1.2.0 should be greater than 1.1.9");
    }

    public void testCompareTo_greaterPatch() {
        Version v1 = new Version(1, 0, 2);
        Version v2 = new Version(1, 0, 1);
        assertTrue(v1.compareTo(v2) > 0, "1.0.2 should be greater than 1.0.1");
    }

    public void testCompareTo_equal() {
        Version v1 = new Version(3, 2, 1);
        Version v2 = new Version(3, 2, 1);
        assertEquals(0, v1.compareTo(v2), "Same versions should compare as 0");
    }

    public void testCompareTo_lessThan() {
        Version v1 = new Version(1, 0, 0);
        Version v2 = new Version(1, 0, 1);
        assertTrue(v1.compareTo(v2) < 0, "1.0.0 should be less than 1.0.1");
    }

    public void testToString() {
        Version v = new Version(1, 2, 3);
        assertEquals("1.2.3", v.toString(), "toString should return semantic version format");
    }
}
