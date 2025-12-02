// src/main/java/com/systeminfo/config/AppConfig.java
package com.systeminfo.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Класс для загрузки и предоставления конфигурации приложения из app.properties.
 * Поддерживает fallback на значения по умолчанию.
 */
public class AppConfig {
    private final Properties props = new Properties();

    /**
     * Конструктор загружает конфигурацию из /app.properties в classpath.
     * Если файл отсутствует, используются значения по умолчанию.
     */
    public AppConfig() {
        try (InputStream is = getClass().getResourceAsStream("/app.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException e) {
            // Логика по умолчанию
        }
    }

    /**
     * @return Единица измерения памяти для вывода (MB или GB). По умолчанию "MB".
     */
    public String getMemoryOutputUnit() {
        return props.getProperty("memory.output.unit", "MB");
    }

    /**
     * @return true, если включена информация о сети. По умолчанию false.
     */
    public boolean isIncludeNetworkInfo() {
        return Boolean.parseBoolean(props.getProperty("include.network.info", "false"));
    }
}
