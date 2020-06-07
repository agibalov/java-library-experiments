package io.agibalov;

import com.google.common.base.Stopwatch;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class StopwatchTest {
    @Test
    public void dummy() throws InterruptedException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Thread.sleep(100);
        stopwatch.stop();
        assertTrue(stopwatch.elapsed(TimeUnit.MILLISECONDS) >= 100);
        System.out.printf("time: %s\n", stopwatch);
    }
}
