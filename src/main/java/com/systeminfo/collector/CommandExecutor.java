// src/main/java/com/systeminfo/collector/CommandExecutor.java
package com.systeminfo.collector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * Утилитный класс для выполнения системных команд с таймаутом.
 * Объединяет stdout и stderr, возвращает trimmed вывод.
 */
public class CommandExecutor {

    private static final Duration TIMEOUT = Duration.of(10, ChronoUnit.SECONDS);

    /**
     * Выполняет команду и возвращает stdout как строку.
     *
     * @param command Массив аргументов команды.
     * @return Тримmed вывод команды.
     * @throws IOException если команда провалилась, таймаут или ошибка запуска.
     */
    public static String execute(String... command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (IOException e) {
            process.destroyForcibly();
            throw e;
        }

        boolean finished = false;
        try {
            finished = process.waitFor(TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            process.destroyForcibly();
            throw new IOException("Command interrupted: " + String.join(" ", command), e);
        }

        if (!finished) {
            process.destroyForcibly();
            throw new IOException("Timeout executing: " + String.join(" ", command));
        }

        int exitCode = process.exitValue();
        if (exitCode != 0) {
            throw new IOException("Command '" + String.join(" ", command) +
                    "' exited with code " + exitCode + ": " + output.toString().trim());
        }

        return output.toString().trim();
    }
}
