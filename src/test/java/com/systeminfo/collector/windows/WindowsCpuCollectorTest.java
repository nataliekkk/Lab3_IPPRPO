// src/test/java/com/systeminfo/collector/windows/WindowsCpuCollectorTest.java
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

/**
 * Unit-тесты для WindowsCpuCollector.
 * Мокирует wmic cpu get Name,NumberOfCores /format:list.
 */
@ExtendWith(MockitoExtension.class)
class WindowsCpuCollectorTest {

    private final AppConfig config = new AppConfig();
    private final WindowsCpuCollector collector = new WindowsCpuCollector();

    @Test
    void parsesSingleCpu() throws IOException {
        String wmicOutput = """
                Name=13th Gen Intel(R) Core(TM) i5-13500HX
                NumberOfCores=14
                
                """;

        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            mockedExecutor.when(() -> CommandExecutor.execute("wmic", "cpu", "get", "Name,NumberOfCores", "/format:list"))
                    .thenReturn(wmicOutput);

            Map<String, Object> cpuData = collector.collect(config);

            assertEquals("13th Gen Intel(R) Core(TM) i5-13500HX", cpuData.get("model"));
            assertEquals(14, cpuData.get("cores"));
        }
    }

    @Test
    void parsesMultipleCpusSumsCores() throws IOException {
        String wmicOutput = """
                Name=Intel(R) Core(TM) i7 CPU
                NumberOfCores=6

                Name=Intel(R) Core(TM) i7 CPU
                NumberOfCores=8
                """;

        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            mockedExecutor.when(() -> CommandExecutor.execute("wmic", "cpu", "get", "Name,NumberOfCores", "/format:list"))
                    .thenReturn(wmicOutput);

            Map<String, Object> cpuData = collector.collect(config);

            assertEquals("Intel(R) Core(TM) i7 CPU", cpuData.get("model"));
            assertEquals(14, cpuData.get("cores"));  // 6 + 8
        }
    }

    @Test
    void handlesEmptyOutput() throws IOException {
        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            mockedExecutor.when(() -> CommandExecutor.execute("wmic", "cpu", "get", "Name,NumberOfCores", "/format:list"))
                    .thenReturn("");

            Map<String, Object> cpuData = collector.collect(config);

            assertEquals("unknown", cpuData.get("model"));
            assertEquals(0, cpuData.get("cores"));
        }
    }

    @Test
    void handlesNoNameFallbackPowershell() throws IOException {
        String wmicOutput = """
                NumberOfCores=14
                """;  // Нет Name

        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            mockedExecutor.when(() -> CommandExecutor.execute("wmic", "cpu", "get", "Name,NumberOfCores", "/format:list"))
                    .thenReturn(wmicOutput);
            mockedExecutor.when(() -> CommandExecutor.execute("powershell", "-Command", "(Get-WmiObject Win32_Processor).Name"))
                    .thenReturn("Intel i5-13500HX");

            Map<String, Object> cpuData = collector.collect(config);

            assertEquals("Intel i5-13500HX", cpuData.get("model"));
            assertEquals(14, cpuData.get("cores"));
        }
    }

    @Test
    void handlesIOException() throws IOException {
        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            mockedExecutor.when(() -> CommandExecutor.execute("wmic", "cpu", "get", "Name,NumberOfCores", "/format:list"))
                    .thenThrow(new IOException("wmic failed"));

            // Ожидаем propagation IOException (как в интерфейсе)
            assertThrows(IOException.class, () -> collector.collect(config));
        }
    }
}
