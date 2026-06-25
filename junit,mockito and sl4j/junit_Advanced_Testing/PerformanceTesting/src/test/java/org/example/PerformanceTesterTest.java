package org.example;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeout;

public class PerformanceTesterTest {

    @Test
    void testPerformTask() {

        PerformanceTester pt = new PerformanceTester();

        assertTimeout(Duration.ofSeconds(2), () -> {
            pt.performTask();
        });
    }
}