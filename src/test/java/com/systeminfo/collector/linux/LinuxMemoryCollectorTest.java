// src/test/java/com/systeminfo/collector/linux/LinuxMemoryCollectorTest.java
package com.systeminfo.collector.linux;

import com.systeminfo.collector.CommandExecutor;
import com.systeminfo.config.AppConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * Unit-тесты для LinuxMemoryCollector.
 * Мокирует grep MemTotal: /proc/meminfo.
 */
@ExtendWith(MockitoExtension.class)
class LinuxMemoryCollectorTest {

    private LinuxMemoryCollector collector;
    private AppConfig mbConfig;
    private AppConfig gbConfig;

    @BeforeEach
    void setUp() {
        mbConfig = new AppConfig() {
            @Override
            public String getMemoryOutputUnit() {
                return "MB";
            }
        };
        gbConfig = new AppConfig() {
            @Override
            public String getMemoryOutputUnit() {
                return "GB";
            }
        };
        collector = new LinuxMemoryCollector();
    }

    /*@Test
    void parsesValidMemTotalMB() throws IOException {
        String meminfoOutput = "MemTotal:       16384000 kB";

        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            mockedExecutor.when(() -> CommandExecutor.execute("grep", "MemTotal:", "/proc/meminfo"))
                    .thenReturn(meminfoOutput);

            Map<String, Object> memoryData = collector.collect(mbConfig);

            assertEquals(15998L, memoryData.get("total_mb"));  // 16384000 kB / 1024 = 15998 MB
        }
    }*/

    /*@Test
    void parsesValidMemTotalGB() throws IOException {
        String meminfoOutput = "MemTotal:       16777216 kB";

        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            mockedExecutor.when(() -> CommandExecutor.execute("grep", "MemTotal:", "/proc/meminfo"))
                    .thenReturn(meminfoOutput);

            Map<String, Object> memoryData = collector.collect(gbConfig);

            assertEquals(15L, memoryData.get("total_gb"));  // 16777216 kB /1024/1024 = 15 GB
        }
    }*/

    @Test
    void handlesNoMatch() throws IOException {
        String meminfoOutput = "MemFree:       8000000 kB";  // Нет MemTotal

        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            mockedExecutor.when(() -> CommandExecutor.execute("grep", "MemTotal:", "/proc/meminfo"))
                    .thenReturn(meminfoOutput);

            // Ожидаем IOException (как в коде)
            assertThrows(IOException.class, () -> collector.collect(mbConfig));
        }
    }

    @Test
    void handlesEmptyOutput() throws IOException {
        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            mockedExecutor.when(() -> CommandExecutor.execute("grep", "MemTotal:", "/proc/meminfo"))
                    .thenReturn("");

            assertThrows(IOException.class, () -> collector.collect(mbConfig));
        }
    }

    @Test
    void handlesCommandFailure() throws IOException {
        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            mockedExecutor.when(() -> CommandExecutor.execute("grep", "MemTotal:", "/proc/meminfo"))
                    .thenThrow(new IOException("grep failed"));

            assertThrows(IOException.class, () -> collector.collect(mbConfig));
        }
    }
}
