package me.loki2302;

import io.github.resilience4j.timelimiter.TimeLimiter;
import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Time limiter - allow for at most M seconds per call, otherwise throw
 */
public class TimeLimiterTest {
    @Test
    public void canUseTimeLimiterBehavior() {
        AtomicLong sleepTime = new AtomicLong();

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        TimeLimiter timeLimiter = TimeLimiter.of(Duration.ofMillis(500));
        Callable<?> runnableWithTimeout = TimeLimiter.decorateFutureSupplier(
                timeLimiter,
                () -> executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(sleepTime.get());
                        } catch (InterruptedException e) {}
                    }
                })
        );

        sleepTime.set(100);
        try {
            runnableWithTimeout.call();
        } catch (Throwable t) {
            fail();
        }

        sleepTime.set(600);
        try {
            runnableWithTimeout.call();
            fail();
        } catch (Throwable t) {
            assertTrue(t instanceof TimeoutException);
        }
    }
}
