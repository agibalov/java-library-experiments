package me.loki2302;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.junit.Test;

import java.time.Duration;
import java.util.function.Supplier;

import static org.junit.Assert.assertTrue;

/**
 * Rate limiter - allow at most M calls within the N seconds duration
 */
public class RateLimiterTest {
    @Test
    public void canUseRateLimiterBehavior() {
        // At most 2 requests per second
        RateLimiter rateLimiter = RateLimiter.of("a", RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(2)
                .build());
        Supplier<Long> rateLimitedSupplier = RateLimiter.decorateSupplier(
                rateLimiter,
                () -> System.currentTimeMillis());

        long t1 = rateLimitedSupplier.get();
        long t2 = rateLimitedSupplier.get();
        long t3 = rateLimitedSupplier.get();

        // t2 and t1 are expected to be nearly the same moment of time
        assertTrue((t2 - t1) < 100);

        // t3 and t2 (and t3 and t1) are expected to have a delay of 1 second
        assertTrue((t3 - t2) > 900);
        assertTrue((t3 - t1) > 900);
    }
}
