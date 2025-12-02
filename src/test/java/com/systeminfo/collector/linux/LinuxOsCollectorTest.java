// src/test/java/com/systeminfo/collector/linux/LinuxOsCollectorTest.java
package com.systeminfo.collector.linux;

import com.systeminfo.collector.CommandExecutor;
import com.systeminfo.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * Unit-тесты для LinuxOsCollector.
 * Мокирует cat /etc/os-release.
 */
@ExtendWith(MockitoExtension.class)
class LinuxOsCollectorTest {

    private final AppConfig config = new AppConfig();
    private final LinuxOsCollector collector = new LinuxOsCollector();

    @Test
    void parsesValidOsRelease() throws IOException {
        String osReleaseContent = """
                PRETTY_NAME="Ubuntu 22.04.4 LTS"
                NAME="Ubuntu"
                VERSION_ID="22.04"
                VERSION="22.04.4 LTS (Noble Numbat)"
                """;

        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            mockedExecutor.when(() -> CommandExecutor.execute("cat", "/etc/os-release"))
                    .thenReturn(osReleaseContent);

            Map<String, Object> osData = collector.collect(config);

            assertEquals("Ubuntu", osData.get("name"));
            assertEquals("22.04", osData.get("version"));
        }
    }

    @Test
    void parsesNameWithParentheses() throws IOException {
        String osReleaseContent = """
                NAME="Ubuntu (GNU/Linux)"
                VERSION_ID="20.04"
                """;

        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            mockedExecutor.when(() -> CommandExecutor.execute("cat", "/etc/os-release"))
                    .thenReturn(osReleaseContent);

            Map<String, Object> osData = collector.collect(config);

            assertEquals("Ubuntu", osData.get("name"));  // Убирает (GNU/Linux)
            assertEquals("20.04", osData.get("version"));
        }
    }

    @Test
    void handlesEmptyFile() throws IOException {
        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            mockedExecutor.when(() -> CommandExecutor.execute("cat", "/etc/os-release"))
                    .thenReturn("");

            Map<String, Object> osData = collector.collect(config);

            assertEquals("Linux", osData.get("name"));
            assertEquals("unknown", osData.get("version"));
        }
    }

    /*@Test
    void handlesIOException() throws IOException {
        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            mockedExecutor.when(() -> CommandExecutor.execute("cat", "/etc/os-release"))
                    .thenThrow(new IOException("File not found"));

            Map<String, Object> osData = collector.collect(config);

            assertEquals("Linux", osData.get("name"));
            assertEquals("unknown", osData.get("version"));
        }
    }*/
}
