package com.systeminfo.detector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class OsDetectorTest {
    @Test
    void detectsLinux() {
        mockOsName("Linux");
        assertEquals("linux", OsDetector.detectOS());
    }

    @Test
    void detectsWindows() {
        mockOsName("Windows");
        assertEquals("windows", OsDetector.detectOS());
    }

    @Test
    void throwsOnUnsupported() {
        mockOsName("Mac OS");
        assertThrows(UnsupportedOperationException.class, () -> OsDetector.detectOS());
    }

    private void mockOsName(String osName) {
        System.setProperty("os.name", osName);
    }
}