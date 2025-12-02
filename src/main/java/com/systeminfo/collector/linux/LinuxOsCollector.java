// LinuxOsCollector.java
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
 * Коллектор информации об ОС для Linux.
 * Парсит /etc/os-release для NAME и VERSION_ID.
 */
public class LinuxOsCollector implements InfoCollector {
    private static final Pattern OS_PATTERN = Pattern.compile("^(NAME|VERSION_ID)=\"([^\"]+)\"");

    @Override
    public Map<String, Object> collect(AppConfig config) throws IOException {
        String output = CommandExecutor.execute("cat", "/etc/os-release");
        Map<String, Object> os = new HashMap<>();
        String name = "Linux";
        String version = "unknown";

        String[] lines = output.split("\n");
        for (String line : lines) {
            Matcher matcher = OS_PATTERN.matcher(line.trim());
            if (matcher.find()) {
                String key = matcher.group(1).toLowerCase();
                String value = matcher.group(2);
                if ("name".equals(key)) {
                    name = value.replace("\"", "").split("\\(")[0].trim(); // Ubuntu из "Ubuntu"
                } else if ("version_id".equals(key)) {
                    version = value.replace("\"", "");
                }
            }
        }

        os.put("name", name);
        os.put("version", version);
        return os;
    }
}
