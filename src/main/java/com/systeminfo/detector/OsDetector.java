// src/main/java/com/systeminfo/detector/OsDetector.java
package com.systeminfo.detector;

import java.util.Locale;

/**
 * Детектор операционной системы на основе системных свойств Java.
 * Поддерживает только Linux и Windows.
 */
public class OsDetector {

    /**
     * Определяет тип ОС.
     * @return "linux" или "windows".
     * @throws UnsupportedOperationException если ОС не поддерживается.
     */
    public static String detectOS() {
        String osName = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        if (osName.contains("windows")) {
            return "windows";
        }
        if (osName.contains("linux")) {
            return "linux";
        }
        throw new UnsupportedOperationException("Unsupported OS: " + osName);
    }
}
