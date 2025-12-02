// src/test/java/com/systeminfo/SystemInfoAppTest.java
package com.systeminfo;

//import com.systeminfo.collector.Collector;  // ← ДОБАВЬТЕ ИМПОРТ Collector (и OsType, если нужно)
import com.systeminfo.detector.OsDetector;  // ← Импорт OsDetector
import com.systeminfo.collector.CollectorFactory;  // ← Импорт Factory
//import com.systeminfo.OsType;


import com.systeminfo.collector.InfoCollector;
import com.systeminfo.config.AppConfig;
import com.systeminfo.detector.OsDetector;
import com.systeminfo.detector.OsType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
/**
 * Интеграционные тесты для SystemInfoApp.
 * Мокирует детекцию ОС, фабрику, коллекторы → проверяет JSON сериализацию.
 */


@ExtendWith(MockitoExtension.class)
class SystemInfoAppTest {

    @Mock
    InfoCollector osCollector;
    @Mock
    InfoCollector cpuCollector;
    @Mock
    InfoCollector memoryCollector;
    @Mock
    AppConfig config;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        outContent.reset();
        errContent.reset();
    }

    @BeforeEach
    void setUpStreams() {
        outContent.reset();
        errContent.reset();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(null);
        System.setErr(null);
    }


    /*@Test
    void mainLogicProducesCorrectJson() throws IOException {  // ← Добавьте throws IOException
        // Mock OsDetector → "linux"
        try (MockedStatic<OsDetector> osDetectorMock = mockStatic(OsDetector.class)) {
            osDetectorMock.when(OsDetector::detectOS).thenReturn("linux");

            // Mock Factory → наши mocks (OsType.LINUX)
            try (MockedStatic<CollectorFactory> factoryMock = mockStatic(CollectorFactory.class)) {
                factoryMock.when(() -> CollectorFactory.getOsCollector(OsType.LINUX)).thenReturn(osCollector);
                factoryMock.when(() -> CollectorFactory.getCpuCollector(OsType.LINUX)).thenReturn(cpuCollector);
                factoryMock.when(() -> CollectorFactory.getMemoryCollector(OsType.LINUX)).thenReturn(memoryCollector);

                // Mock collectors data (doReturn → НЕ вызывает реальный метод, нет IOException!)
                doReturn(Map.of("name", "Ubuntu", "version", "22.04")).when(osCollector).collect(any());
                doReturn(Map.of("model", "Intel i7", "cores", 12)).when(cpuCollector).collect(any());
                doReturn(Map.of("total_mb", 15998)).when(memoryCollector).collect(any());

                // Запуск логики main (НЕ бросает Exception — ловит внутри, возвращает int)
                int exitCode = SystemInfoApp.mainLogic();
                assertEquals(0, exitCode);  // Успех

                // Проверка JSON (compact)
                String jsonOutput = outContent.toString().trim();
                assertTrue(jsonOutput.contains("\"os\":{\"name\":\"Ubuntu\",\"version\":\"22.04\"}"));
                assertTrue(jsonOutput.contains("\"cpu\":{\"model\":\"Intel i7\",\"cores\":12}"));
                assertTrue(jsonOutput.contains("\"memory\":{\"total_mb\":15998}"));
                assertEquals("", errContent.toString().trim());  // Нет ошибок
            }
        }
    }*/

    @Test
    void handlesCollectorException() throws IOException {
        try (MockedStatic<OsDetector> osDetectorMock = mockStatic(OsDetector.class);
             MockedStatic<CollectorFactory> factoryMock = mockStatic(CollectorFactory.class)) {

            osDetectorMock.when(OsDetector::detectOS).thenReturn("linux");
            factoryMock.when(() -> CollectorFactory.getOsCollector(any(OsType.class))).thenReturn(osCollector);

            doThrow(new RuntimeException("Collector fail")).when(osCollector).collect(any());

            int exitCode = SystemInfoApp.mainLogic();
            assertEquals(1, exitCode);  // Ошибка

            String errOutput = errContent.toString().trim();
            assertTrue(errOutput.contains("Error collecting system info"));
            assertTrue(errOutput.contains("Collector fail"));
            assertEquals("", outContent.toString().trim());  // Нет JSON
        }
    }
}
