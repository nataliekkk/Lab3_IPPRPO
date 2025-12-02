// LinuxMemoryCollector.java
package com.systeminfo.collector.linux;

import com.systeminfo.collector.CommandExecutor;
import com.systeminfo.collector.InfoCollector;
import com.systeminfo.config.AppConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Коллектор информации о RAM для Linux (/proc/meminfo).
 * MemTotal в kB -> конвертирует в MB/GB по конфигу.
 */
public class LinuxMemoryCollector implements InfoCollector {
    private static final Pattern MEM_PATTERN = Pattern.compile("MemTotal:\\s+(\\d+)\\s+kB");

    @Override
    public Map<String, Object> collect(AppConfig config) throws IOException {
        String output = CommandExecutor.execute("grep", "MemTotal:", "/proc/meminfo");
        Matcher matcher = MEM_PATTERN.matcher(output);
        if (!matcher.find()) {
            throw new IOException("Failed to parse MemTotal");
        }
        long kb = Long.parseLong(matcher.group(1));
        long total = convertToUnit(kb / 1024, config.getMemoryOutputUnit()); // kB -> MB/GB

        Map<String, Object> memory = new HashMap<>();
        String key = "total_" + config.getMemoryOutputUnit().toLowerCase();
        memory.put(key, total);
        return memory;
    }

    private long convertToUnit(long mb, String unit) {
        return "GB".equalsIgnoreCase(unit) ? mb / 1024 : mb;
    }
}
