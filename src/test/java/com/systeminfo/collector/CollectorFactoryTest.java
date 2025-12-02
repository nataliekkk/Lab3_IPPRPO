package com.systeminfo.collector;

import com.systeminfo.collector.linux.LinuxCpuCollector;
import com.systeminfo.detector.OsType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollectorFactoryTest {
    @Test
    void createsLinuxCollectors() {
        assertInstanceOf(LinuxCpuCollector.class, CollectorFactory.getCpuCollector(OsType.LINUX));
        // Аналогично для os/memory...
    }

    @Test
    void createsWindowsCollectors() {
        assertInstanceOf(com.systeminfo.collector.windows.WindowsCpuCollector.class, CollectorFactory.getCpuCollector(OsType.WINDOWS));
    }
}
