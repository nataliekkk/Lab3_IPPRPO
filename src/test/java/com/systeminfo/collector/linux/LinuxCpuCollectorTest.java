package com.systeminfo.collector.linux;

import com.systeminfo.collector.CommandExecutor;
import com.systeminfo.config.AppConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LinuxCpuCollectorTest {
    @InjectMocks
    LinuxCpuCollector collector;

    @Test
    void parsesLscpu() throws IOException {
        try (MockedStatic<CommandExecutor> mock = mockStatic(CommandExecutor.class)) {
            mock.when(() -> CommandExecutor.execute("lscpu"))
                    .thenReturn("""
                    Model name:              Intel(R) Core(TM) i7
                    CPU(s):                  12
                    """);

            Map<String, Object> cpu = collector.collect(new AppConfig());
            assertEquals("Intel(R) Core(TM) i7", cpu.get("model"));
            assertEquals(12, cpu.get("cores"));
        }
    }

    @Test
    void handlesEmptyOutput() throws IOException {
        try (MockedStatic<CommandExecutor> mock = mockStatic(CommandExecutor.class)) {
            mock.when(() -> CommandExecutor.execute("lscpu")).thenReturn("");

            Map<String, Object> cpu = collector.collect(new AppConfig());
            assertEquals("unknown", cpu.get("model"));
            assertEquals(0, cpu.get("cores"));
        }
    }
}
