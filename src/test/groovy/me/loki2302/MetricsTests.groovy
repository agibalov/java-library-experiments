package me.loki2302

import com.codahale.metrics.Gauge
import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.health.HealthCheck
import com.codahale.metrics.health.HealthCheckRegistry
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class MetricsTests {
    private final static MillisecondsInOneSecond = 1000

    @Test
    void canUseMeter() {
        def metrics = new MetricRegistry()
        def meter = metrics.meter('meter one')

        final numberOfRepetitions = 100
        final delayInMilliseconds = 100

        numberOfRepetitions.times {
            meter.mark()
            Thread.sleep(delayInMilliseconds)
        }

        assertEquals(numberOfRepetitions, meter.count)
        assertEquals(MillisecondsInOneSecond / delayInMilliseconds, meter.meanRate, 0.2)
    }

    @Test
    void canUseGauge() {
        def metrics = new MetricRegistry()
        def gauge1 = metrics.register(MetricRegistry.name("gauge1"), [
                getValue: { 1 }
        ] as Gauge<Integer>)

        assertEquals(1, gauge1.value)
    }

    @Test
    void canUseCounter() {
        def metrics = new MetricRegistry()
        def counter1 = metrics.counter("counter1")
        counter1.inc()
        counter1.inc()
        counter1.dec()

        assertEquals(1, counter1.count)
    }

    @Test
    void canUseHistogram() {
        def metrics = new MetricRegistry()
        def histogram1 = metrics.histogram("histogram1")

        histogram1.update(-10)
        histogram1.update(0)
        histogram1.update(10)

        def snapshot = histogram1.snapshot
        assertEquals(-10, snapshot.min)
        assertEquals(0, snapshot.mean, 0.001)
        assertEquals(10, snapshot.max)
        assertEquals(0, snapshot.median, 0.001)
    }

    @Test
    void canUseTimer() {
        def metrics = new MetricRegistry()
        def timer1 = metrics.timer("timer1")

        final numberOfRepetitions = 100
        final delayInMilliseconds = 100

        numberOfRepetitions.times {
            def context = timer1.time()
            Thread.sleep(delayInMilliseconds)
            context.stop()
        }

        def snapshot = timer1.snapshot
        assertEquals(100, timer1.count)

        final nanosecondsInMillisecond = 1000000
        assertEquals(delayInMilliseconds, snapshot.min / nanosecondsInMillisecond, 10.0)
        assertEquals(delayInMilliseconds, snapshot.mean / nanosecondsInMillisecond, 1)
        assertEquals(delayInMilliseconds, snapshot.max / nanosecondsInMillisecond, 10.0)
    }

    @Test
    void canUseHealthChecks() {
        def healthCheckRegistry = new HealthCheckRegistry()
        healthCheckRegistry.register("Service A", new HealthCheck() {
            @Override
            protected HealthCheck.Result check() throws Exception {
                return HealthCheck.Result.healthy()
            }
        })

        healthCheckRegistry.register("Service B", new HealthCheck() {
            @Override
            protected HealthCheck.Result check() throws Exception {
                return HealthCheck.Result.unhealthy('totally broken')
            }
        })

        def healthChecks = healthCheckRegistry.runHealthChecks()
        def messages = healthChecks.collect { name, result ->
            "${name} is ${result.healthy ? 'healthy' : result.message}".toString()
        }

        assertTrue(messages.contains('Service A is healthy'))
        assertTrue(messages.contains('Service B is totally broken'))
    }
}
