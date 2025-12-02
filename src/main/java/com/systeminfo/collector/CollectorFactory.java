// src/main/java/com/systeminfo/collector/CollectorFactory.java
package com.systeminfo.collector;

import com.systeminfo.collector.linux.LinuxCpuCollector;
import com.systeminfo.collector.linux.LinuxMemoryCollector;
import com.systeminfo.collector.linux.LinuxOsCollector;
import com.systeminfo.collector.windows.WindowsCpuCollector;
import com.systeminfo.collector.windows.WindowsMemoryCollector;
import com.systeminfo.collector.windows.WindowsOsCollector;
import com.systeminfo.detector.OsType;

/**
 * Фабрика для создания коллекторов информации в зависимости от типа ОС.
 * Обеспечивает расширяемость: добавление новой ОС требует только нового enum значения
 * и соответствующих классов коллекторов.
 */
public class CollectorFactory {

    /**
     * Создает коллектор для информации об ОС.
     * @param osType Тип ОС.
     * @return Инстанс коллектора.
     */
    public static InfoCollector getOsCollector(OsType osType) {
        return switch (osType) {
            case LINUX -> new LinuxOsCollector();
            case WINDOWS -> new WindowsOsCollector();
        };
    }

    /**
     * Создает коллектор для информации о CPU.
     * @param osType Тип ОС.
     * @return Инстанс коллектора.
     */
    public static InfoCollector getCpuCollector(OsType osType) {
        return switch (osType) {
            case LINUX -> new LinuxCpuCollector();
            case WINDOWS -> new WindowsCpuCollector();
        };
    }

    /**
     * Создает коллектор для информации о памяти.
     * @param osType Тип ОС.
     * @return Инстанс коллектора.
     */
    public static InfoCollector getMemoryCollector(OsType osType) {
        return switch (osType) {
            case LINUX -> new LinuxMemoryCollector();
            case WINDOWS -> new WindowsMemoryCollector();
        };
    }
}