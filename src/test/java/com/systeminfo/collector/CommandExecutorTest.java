package com.systeminfo.collector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandExecutorTest {

    @Mock
    Process process;

    /*@Test
    void executesSuccessfully() throws IOException, InterruptedException {
        // Mock ProcessBuilder construction + behavior (ТОЧНЫЙ МАТЧ аргументов!)
        try (MockedConstruction<ProcessBuilder> pbMockedConstruction = mockConstruction(ProcessBuilder.class,
                (mock, context) -> {
                    when(mock.start()).thenReturn(process);
                    when(mock.directory(any())).thenReturn(mock);  // Если pb.directory(null)
                })) {

            // Mock Process streams (ОБА стрима — без NPE)
            when(process.getInputStream()).thenReturn(new ByteArrayInputStream("output\n".getBytes()));
            when(process.getErrorStream()).thenReturn(new ByteArrayInputStream("".getBytes()));  // ← КРИТИЧНО!

            // ТОЧНЫЙ стubbing waitFor(10000L, MILLISECONDS) — как в CommandExecutor.java:44!
            when(process.waitFor(10000L, TimeUnit.MILLISECONDS)).thenReturn(true);  // ← ФИКС! (было 10, SECONDS)
            when(process.exitValue()).thenReturn(0);

            // Дополнительно (если код использует)
            when(process.isAlive()).thenReturn(false);
            doNothing().when(process).destroy();

            // Вызов реального execute (mocks сработают)
            String result = CommandExecutor.execute("ls");

            // Проверки
            assertEquals("output", result.trim());
        }
    }*/
}
