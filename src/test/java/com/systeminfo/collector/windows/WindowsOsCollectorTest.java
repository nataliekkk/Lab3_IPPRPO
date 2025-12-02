package com.systeminfo.collector.windows;

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

@ExtendWith(MockitoExtension.class)
class WindowsOsCollectorTest {
    @Test
    void parsesSysteminfo() throws IOException {
        try (MockedStatic<CommandExecutor> mock = mockStatic(CommandExecutor.class)) {
            mock.when(() -> CommandExecutor.execute("systeminfo"))
                    .thenReturn("""
                    OS Name:                   Microsoft Windows 11 Pro
                    OS Version:                10.0.22631 N/A Build 22631
                    """);

            Map<String, Object> os = new WindowsOsCollector().collect(new AppConfig());
            assertEquals("Windows 11", os.get("name"));
            assertEquals("10.0.22631", os.get("version"));
        }
    }

    @Test
    void fallbackJavaProps() throws IOException {
        // Mock System.getProperty (PowerMockito или reflection, но просто test fallback)
        // В реале: использует Java props если exception
    }
}
