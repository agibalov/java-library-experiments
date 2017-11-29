package me.loki2302;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.vavr.control.Try;
import org.junit.Test;

import java.time.Duration;
import java.util.function.Supplier;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Circuit breaker - if supplier stops working, stop calling it for a while
 */
public class CircuitBreakerTest {
    @Test
    public void canUseCircuitBreakerBehavior() throws InterruptedException {
        Supplier unreliableSupplier = mock(Supplier.class);

        CircuitBreaker circuitBreaker = CircuitBreaker.of(
                "a",
                CircuitBreakerConfig.custom()
                        .failureRateThreshold(0.5f)
                        .ringBufferSizeInClosedState(3)
                        .ringBufferSizeInHalfOpenState(3)
                        .waitDurationInOpenState(Duration.ofSeconds(1))
                        .build());
        Supplier<String> reliableSupplier = CircuitBreaker.decorateSupplier(
                circuitBreaker,
                unreliableSupplier);

        reset(unreliableSupplier);
        when(unreliableSupplier.get()).thenReturn("hello");
        assertTrue(Try.ofSupplier(reliableSupplier).isSuccess());
        verify(unreliableSupplier, times(1)).get();
        assertEquals(CircuitBreaker.State.CLOSED, circuitBreaker.getState());

        reset(unreliableSupplier);
        when(unreliableSupplier.get()).thenThrow(new RuntimeException("error1"));
        assertFalse(Try.ofSupplier(reliableSupplier).isSuccess());
        verify(unreliableSupplier, times(1)).get();
        assertEquals(CircuitBreaker.State.CLOSED, circuitBreaker.getState());

        reset(unreliableSupplier);
        when(unreliableSupplier.get()).thenThrow(new RuntimeException("error2"));
        assertFalse(Try.ofSupplier(reliableSupplier).isSuccess());
        verify(unreliableSupplier, times(1)).get();
        assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());

        reset(unreliableSupplier);
        when(unreliableSupplier.get()).thenReturn("hello");
        assertFalse(Try.ofSupplier(reliableSupplier).isSuccess());
        verify(unreliableSupplier, times(0)).get();
        assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());

        Thread.sleep(2000);

        reset(unreliableSupplier);
        when(unreliableSupplier.get()).thenReturn("hello");
        assertTrue(Try.ofSupplier(reliableSupplier).isSuccess());
        verify(unreliableSupplier, times(1)).get();
        assertEquals(CircuitBreaker.State.HALF_OPEN, circuitBreaker.getState());

        reset(unreliableSupplier);
        when(unreliableSupplier.get()).thenReturn("hello");
        assertTrue(Try.ofSupplier(reliableSupplier).isSuccess());
        verify(unreliableSupplier, times(1)).get();
        assertEquals(CircuitBreaker.State.HALF_OPEN, circuitBreaker.getState());

        reset(unreliableSupplier);
        when(unreliableSupplier.get()).thenReturn("hello");
        assertTrue(Try.ofSupplier(reliableSupplier).isSuccess());
        verify(unreliableSupplier, times(1)).get();
        assertEquals(CircuitBreaker.State.CLOSED, circuitBreaker.getState());
    }
}
