// src/main/java/com/systeminfo/SystemInfoApp.java
package com.systeminfo;

import com.fasterxml.jackson.databind.ObjectMapper;  // ← Это появится после sync
import com.systeminfo.config.AppConfig;
import com.systeminfo.collector.CollectorFactory;
import com.systeminfo.collector.InfoCollector;
import com.systeminfo.detector.OsDetector;
import com.systeminfo.detector.OsType;

import java.util.HashMap;
import java.util.Map;

/**
 * Главный класс приложения для сбора системной информации.
 * Запускает сбор данных об ОС, CPU и памяти в зависимости от платформы
 * и выводит результат в формате JSON на stdout.
 */

public class SystemInfoApp {
    public static void main(String[] args) {
        System.exit(mainLogic());
    }

    /**
     * Логика main для unit-тестов (без System.exit).
     * Возвращает 0 (успех) или 1 (ошибка).
     */
    public static int mainLogic() {
        try {
            OsType osType = OsType.valueOf(OsDetector.detectOS().toUpperCase());
            AppConfig config = new AppConfig();

            InfoCollector osCollector = CollectorFactory.getOsCollector(osType);
            InfoCollector cpuCollector = CollectorFactory.getCpuCollector(osType);
            InfoCollector memoryCollector = CollectorFactory.getMemoryCollector(osType);

            Map<String, Object> osData = osCollector.collect(config);
            Map<String, Object> cpuData = cpuCollector.collect(config);
            Map<String, Object> memoryData = memoryCollector.collect(config);

            Map<String, Object> systemInfo = new HashMap<>();
            systemInfo.put("os", osData);
            systemInfo.put("cpu", cpuData);
            systemInfo.put("memory", memoryData);

            // Заглушка для сетевой информации (если включено в будущем)
            if (config.isIncludeNetworkInfo()) {
                // systemInfo.put("network", collectNetworkInfo());
            }

            ObjectMapper mapper = new ObjectMapper();
            // Вывод в компактном JSON (без лишних пробелов)
            mapper.writeValue(System.out, systemInfo);
            return 0;  // Успех
        } catch (Exception e) {
            System.err.println("Error collecting system info: " + e.getMessage());
            return 1;  // Ошибка
        }
    }
}
