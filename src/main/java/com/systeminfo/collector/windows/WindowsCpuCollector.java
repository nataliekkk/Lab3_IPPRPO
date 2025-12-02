// src/main/java/com/systeminfo/collector/windows/WindowsCpuCollector.java
package com.systeminfo.collector.windows;

import com.systeminfo.collector.CommandExecutor;
import com.systeminfo.collector.InfoCollector;  // ← ДОБАВЛЕН импорт
import com.systeminfo.config.AppConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Коллектор информации о CPU для Windows (wmic cpu get Name,NumberOfCores /format:list).
 * Парсит вывод wmic в формате list для надёжности.
 */
public class WindowsCpuCollector implements InfoCollector {  // ← ДОБАВЛЕН implements InfoCollector

    @Override
    public Map<String, Object> collect(AppConfig config) throws IOException {
        // Используем /format:list для чистого вывода (Name=..., NumberOfCores=...)
        String output = CommandExecutor.execute("wmic", "cpu", "get", "Name,NumberOfCores", "/format:list");

        Map<String, Object> cpu = new HashMap<>();
        String model = "unknown";
        int cores = 0;
        int totalCores = 0;  // Суммируем по всем CPU (логические ядра)

        // wmic выводит блоки: Name=Intel... \n NumberOfCores=6 \n \n Name=Intel... и т.д.
        String[] blocks = output.split("\n\n");  // Разделяем по блокам CPU
        for (String block : blocks) {
            if (block.trim().isEmpty()) continue;

            // Парсим Name
            Pattern namePat = Pattern.compile("Name=(.+)");
            Matcher nameMatcher = namePat.matcher(block);
            if (nameMatcher.find()) {
                model = nameMatcher.group(1).trim().replaceAll("\\\\", "/");  // Очистка escape
            }

            // Парсим NumberOfCores (по CPU)
            Pattern coresPat = Pattern.compile("NumberOfCores=(\\d+)");
            Matcher coresMatcher = coresPat.matcher(block);
            if (coresMatcher.find()) {
                totalCores += Integer.parseInt(coresMatcher.group(1));
            }
        }

        // Если model не найден, fallback на powershell
        if ("unknown".equals(model)) {
            try {
                model = CommandExecutor.execute("powershell", "-Command", "(Get-WmiObject Win32_Processor).Name").trim();
            } catch (Exception ignored) {}
        }

        cores = totalCores;  // Общее количество ядер

        cpu.put("model", model);
        cpu.put("cores", cores);
        return cpu;
    }
}
