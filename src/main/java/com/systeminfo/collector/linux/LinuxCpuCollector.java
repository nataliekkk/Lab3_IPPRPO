// LinuxCpuCollector.java
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
 * Коллектор информации о CPU для Linux (lscpu).
 * Извлекает модель и общее количество CPU (ядер).
 */
public class LinuxCpuCollector implements InfoCollector {
    private static final Pattern MODEL_PATTERN = Pattern.compile("Model name:\\s+(.+)");
    private static final Pattern CORES_PATTERN = Pattern.compile("CPU\\(s\\):\\s+(\\d+)");

    @Override
    public Map<String, Object> collect(AppConfig config) throws IOException {
        String output = CommandExecutor.execute("lscpu");
        Map<String, Object> cpu = new HashMap<>();
        String model = "unknown";
        int cores = 0;

        String[] lines = output.split("\n");
        for (String line : lines) {
            Matcher modelMatcher = MODEL_PATTERN.matcher(line);
            if (modelMatcher.find()) {
                model = modelMatcher.group(1).trim();
            }
            Matcher coresMatcher = CORES_PATTERN.matcher(line);
            if (coresMatcher.find()) {
                cores = Integer.parseInt(coresMatcher.group(1));
            }
        }

        cpu.put("model", model);
        cpu.put("cores", cores);
        return cpu;
    }
}