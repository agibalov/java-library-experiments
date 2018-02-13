package io.agibalov;

import lombok.SneakyThrows;
import lombok.Synchronized;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class SynchronizedTest {
    // NOTE: not a very reliable way to show why I have @Synchronized there, but still...
    @Test
    public void canUseSynchronized() throws InterruptedException {
        Counter counter = new Counter();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for(int i = 0; i < 3; ++i) {
            executorService.submit(() -> {
                for (int j = 0; j < 100; ++j) {
                    counter.increment();
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        assertEquals(300, counter.count);
    }

    private static class Counter {
        private int count = 0;

        @Synchronized
        @SneakyThrows
        public void increment() {
            Thread.sleep(10);
            ++count;
        }
    }
}
