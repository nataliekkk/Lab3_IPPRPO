package com.systeminfo.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {
    @Test
    void defaultsWhenNoFile() {
        AppConfig config = new AppConfig();
        assertEquals("MB", config.getMemoryOutputUnit());
        assertFalse(config.isIncludeNetworkInfo());
    }

    /*@Test
    void loadsProperties(@TempDir Path tempDir) throws IOException {
        Path props = tempDir.resolve("app.properties");
        Files.writeString(props, "memory.output.unit=GB\ninclude.network.info=true");
        // Загружаем из classpath (mock не нужен, используем reflection или test resources)
        AppConfig config = new AppConfig();
        assertEquals("GB", config.getMemoryOutputUnit());
        assertTrue(config.isIncludeNetworkInfo());
    }*/
}