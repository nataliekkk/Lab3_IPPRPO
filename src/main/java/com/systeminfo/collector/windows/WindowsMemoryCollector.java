// WindowsMemoryCollector.java
package com.systeminfo.collector.windows;

import com.systeminfo.collector.CommandExecutor;
import com.systeminfo.collector.InfoCollector;
import com.systeminfo.config.AppConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Коллектор информации о RAM для Windows (wmic).
 * TotalPhysicalMemory в bytes -> MB/GB.
 */
public class WindowsMemoryCollector implements InfoCollector {
    private static final Pattern MEM_PATTERN = Pattern.compile("TotalPhysicalMemory\\s+(\\d+)");

    @Override
    public Map<String, Object> collect(AppConfig config) throws IOException {
        String output = CommandExecutor.execute("wmic", "computersystem", "get", "TotalPhysicalMemory");
        Matcher matcher = MEM_PATTERN.matcher(output);
        if (!matcher.find()) {
            throw new IOException("Failed to parse TotalPhysicalMemory");
        }
        long bytes = Long.parseLong(matcher.group(1));
        long mb = bytes / (1024 * 1024);
        long total = mb / 1024; // GB if needed

        Map<String, Object> memory = new HashMap<>();
        String unit = config.getMemoryOutputUnit();
        String key = "total_" + unit.toLowerCase();
        memory.put(key, "GB".equalsIgnoreCase(unit) ? total : mb);
        return memory;
    }
}
