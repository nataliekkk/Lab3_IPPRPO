/*package com.systeminfo.collector.windows;

import com.systeminfo.collector.CommandExecutor;
import com.systeminfo.config.AppConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // ← ОБЯЗАТЕЛЬНО: Инициализирует @Mock
class WindowsMemoryCollectorTest {

    private WindowsMemoryCollector collector;

    @Mock  // ← @Mock вместо new AppConfig() (фиксит NPE + все методы)
    AppConfig gbConfig;

    @BeforeEach
    void setUp() {
        collector = new WindowsMemoryCollector();*/

        // Стубы для AppConfig (только нужные — остальные дефолтные)
        //when(gbConfig.getMemoryOutputUnit()).thenReturn("GB");
        // Если нужны другие (по коду collector):
        // when(gbConfig.getTimeoutMs()).thenReturn(10000L);
        // when(gbConfig.getMemoryDivider()).thenReturn(1024L * 1024L);
    //}

    /*@Test
    void parsesValidMemoryGB() throws IOException {
        String wmicOutput = """
                TotalVisibleMemorySize=17179869184
                """;  // 17179869184 KB / 1024^2 = 16384 MB / 1024 = 16 GB ✅

        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            // ТОЧНЫЙ матч: execute("wmic", "OS", "get", "TotalVisibleMemorySize", "/value") — 5 аргументов
            mockedExecutor.when(() -> CommandExecutor.execute("wmic", "OS", "get", "TotalVisibleMemorySize", "/value"))
                    .thenReturn(wmicOutput);

            Map<String, Object> memoryData = collector.collect(gbConfig);

            assertEquals(16L, memoryData.get("total_gb"));
            assertEquals(1, memoryData.size());  // Только один ключ
        }
    }*/
//}

    /*@Test
    void parsesValidMemoryMB() throws IOException {
        String wmicOutput = """
                TotalVisibleMemorySize=16537216
                """;

        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            // ← ТОЧНЫЙ матч 5 аргументов (БЕЗ timeout!) — реальный вызов в collector
            mockedExecutor.when(() -> CommandExecutor.execute("wmic", "OS", "get", "TotalVisibleMemorySize", "/value"))
                    .thenReturn(wmicOutput);

            Map<String, Object> memoryData = collector.collect(mbConfig);

            assertEquals(16123L, memoryData.get("total_mb"));  // KB → MB
            assertEquals(1, memoryData.size());
        }
    }

    @Test
    void parsesValidMemoryGB() throws IOException {
        String wmicOutput = """
                TotalVisibleMemorySize=16537216
                """;

        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            mockedExecutor.when(() -> CommandExecutor.execute("wmic", "OS", "get", "TotalVisibleMemorySize", "/value"))
                    .thenReturn(wmicOutput);

            Map<String, Object> memoryData = collector.collect(gbConfig);

            assertEquals(15L, memoryData.get("total_gb"));  // KB → GB
            assertEquals(1, memoryData.size());
        }
    }*/

    /*@Test
    void parsesValidMemoryGB() throws IOException {
        String wmicOutput = """
                TotalVisibleMemorySize=17179869184
                """;

        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            mockedExecutor.when(() -> CommandExecutor.execute("wmic", "OS", "get", "TotalVisibleMemorySize", "/value"))
                    .thenReturn(wmicOutput);

            Map<String, Object> memoryData = collector.collect(gbConfig);

            assertEquals(16L, memoryData.get("total_gb"));  // 17179869184 KB /1024/1024 = 16 GB
        }
    }*/

    /*@Test
    void handlesNoMatch() throws IOException {
        String wmicOutput = """
                OtherProperty=12345
                """;  // Нет TotalVisibleMemorySize

        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            mockedExecutor.when(() -> CommandExecutor.execute("wmic", "OS", "get", "TotalVisibleMemorySize", "/value"))
                    .thenReturn(wmicOutput);

            assertThrows(IOException.class, () -> collector.collect(mbConfig));
        }
    }*/

    /*@Test
    void handlesEmptyOutput() throws IOException {
        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            mockedExecutor.when(() -> CommandExecutor.execute("wmic", "OS", "get", "TotalVisibleMemorySize", "/value"))
                    .thenReturn("");

            assertThrows(IOException.class, () -> collector.collect(mbConfig));
        }
    }*/

    /*@Test
    void handlesCommandFailure() throws IOException {
        try (MockedStatic<CommandExecutor> mockedExecutor = mockStatic(CommandExecutor.class)) {
            mockedExecutor.when(() -> CommandExecutor.execute("wmic", "OS", "get", "TotalVisibleMemorySize", "/value"))
                    .thenThrow(new IOException("wmic failed"));

            assertThrows(IOException.class, () -> collector.collect(mbConfig));
        }
    }*/
//}
