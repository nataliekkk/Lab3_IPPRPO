// src/main/java/com/systeminfo/collector/InfoCollector.java
package com.systeminfo.collector;

import com.systeminfo.config.AppConfig;

import java.io.IOException;
import java.util.Map;

/**
 * Интерфейс для коллекторов системной информации.
 * Каждый коллектор возвращает Map в формате, подходящем для JSON-сериализации.
 */
public interface InfoCollector {
    /**
     * Собирает информацию о компоненте системы.
     * @param config Конфигурация приложения (используется для памяти).
     * @return Map с данными (name/value пары).
     * @throws IOException при ошибках выполнения команд или парсинга.
     */
    Map<String, Object> collect(AppConfig config) throws IOException;
}
