package me.loki2302;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.junit.Test;

import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Retry - if supplier stops working, try again
 */
public class RetryTest {
    @Test
    public void canUseRetryBehavior() {
        Supplier unreliableSupplier = mock(Supplier.class);

        Retry retry = Retry.of(
                "a",
                RetryConfig.custom()
                        .maxAttempts(3)
                        .build());
        Supplier<String> supplier = Retry.decorateSupplier(
                retry,
                unreliableSupplier);

        when(unreliableSupplier.get())
                .thenThrow(new RuntimeException("error1"))
                .thenThrow(new RuntimeException("error2"))
                .thenReturn("hello there");

        String s = supplier.get();
        assertEquals("hello there", s);

        Retry.Metrics metrics = retry.getMetrics();
        assertEquals(0, metrics.getNumberOfSuccessfulCallsWithoutRetryAttempt());
        assertEquals(0, metrics.getNumberOfFailedCallsWithoutRetryAttempt());
        assertEquals(1, metrics.getNumberOfSuccessfulCallsWithRetryAttempt());
        assertEquals(0, metrics.getNumberOfFailedCallsWithRetryAttempt());
    }
}
