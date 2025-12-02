// src/main/java/com/systeminfo/collector/windows/WindowsOsCollector.java
package com.systeminfo.collector.windows;

import com.systeminfo.collector.CommandExecutor;
import com.systeminfo.collector.InfoCollector;
import com.systeminfo.config.AppConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Коллектор информации об ОС для Windows.
 * 1. Парсит 'systeminfo' (startsWith без regex).
 * 2. Fallback: Java props (всегда работает).
 * 3. Fallback: PowerShell (если нужно).
 */
public class WindowsOsCollector implements InfoCollector {

    @Override
    public Map<String, Object> collect(AppConfig config) throws IOException {
        Map<String, Object> os = new HashMap<>();
        String name = "Windows";
        String version = "unknown";

        // Fallback 1: Java системные свойства (БЫСТРО, всегда работает)
        name = System.getProperty("os.name", "Windows").replaceAll("(?i)windows", "Windows").trim();
        version = System.getProperty("os.version", "unknown");

        try {
            // Основной: systeminfo (детальный парсинг)
            String output = CommandExecutor.execute("systeminfo");
            String[] lines = output.split("\n");  // ← ЗДЕСЬ lines объявлен!
            for (String line : lines) {
                String trimmedLine = line.trim();

                // OS Name (EN/RU)
                if (trimmedLine.startsWith("OS Name:") || trimmedLine.startsWith("Имя ОС:")) {
                    name = extractAfterColon(trimmedLine);
                    if (name.contains("11")) name = "Windows 11";  // Нормализация
                    else if (name.contains("10")) name = "Windows 10";
                }
                // OS Version (EN/RU)
                if (trimmedLine.startsWith("OS Version:") || trimmedLine.startsWith("Версия ОС:")) {
                    String verFull = extractAfterColon(trimmedLine);
                    // "10.0.22631 N/A Build 22631.4169" → "10.0.22631"
                    String[] verParts = verFull.split("\\s+");
                    if (verParts.length > 0) {
                        version = verParts[0];
                    }
                    break;
                }
            }
        } catch (Exception ignored) {
            // Fallback 2: PowerShell (если systeminfo недоступно)
            try {
                String psOutput = CommandExecutor.execute("powershell", "-Command", "ver");
                // "Microsoft Windows [Version 10.0.22631.4169]" → extract version
                if (psOutput.contains("Version")) {
                    String verPart = psOutput.substring(psOutput.indexOf("Version") + 7).trim();
                    version = verPart.split("\\s+")[0];
                }
            } catch (Exception ignored2) {
                // Версия из Java уже установлена выше
            }
        }

        os.put("name", name);
        os.put("version", version);
        return os;
    }

    /**
     * Извлекает значение после ':' (robust, без regex).
     */
    private String extractAfterColon(String line) {
        int colonIndex = line.indexOf(':');
        return (colonIndex > 0) ? line.substring(colonIndex + 1).trim() : "unknown";
    }
}
