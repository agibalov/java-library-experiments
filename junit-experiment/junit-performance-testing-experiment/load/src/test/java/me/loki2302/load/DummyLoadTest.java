package me.loki2302.load;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import me.loi2302.app.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;

/**
 * Not some real test - mostly useful as an idea how to generate "N requests per second"
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DummyLoadTest {
    // Should be large enough to handle the expected number of parallel requests
    private final int NUMBER_OF_WORKER_THREADS = 20;
    private final long TRIGGER_PERIOD_MS = 100;
    private final long SESSION_RUN_TIME_MS = Duration.ofMinutes(1).toMillis();

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void canHandle10RequestsPerSecond() throws InterruptedException {
        MetricRegistry metricRegistry = new MetricRegistry();
        Meter workerMeter = metricRegistry.meter("worker");

        ScheduledExecutorService triggerExecutor = Executors.newSingleThreadScheduledExecutor();

        ExecutorService workExecutor = Executors.newFixedThreadPool(NUMBER_OF_WORKER_THREADS);

        // Should always be 0. If it's not 0, the test doesn't produce the expected load.
        // If it happens, increase the NUMBER_OF_WORKER_THREADS or decrease the TRIGGER_PERIOD_MS
        metricRegistry.register("workExecutorQueueLength",
                (Gauge<Integer>) () -> ((ThreadPoolExecutor)workExecutor).getQueue().size());

        ScheduledFuture<?> triggerFuture = triggerExecutor.scheduleAtFixedRate(() -> {
            workExecutor.submit(() -> {
                workerMeter.mark();

                // Thread.sleep(500);
                restTemplate.getForObject("http://localhost:8080/api/notes/1", App.NoteDto.class);
            });
        }, 0, TRIGGER_PERIOD_MS, TimeUnit.MILLISECONDS);

        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        consoleReporter.start(1, TimeUnit.SECONDS);

        Thread.sleep(SESSION_RUN_TIME_MS);

        triggerFuture.cancel(true);
        triggerExecutor.shutdown();

        workExecutor.shutdown();

        assertEquals(600, workerMeter.getCount(), 10.0);
        assertEquals(10, workerMeter.getOneMinuteRate(), 1);
    }

    @Configuration
    @Import(App.class)
    public static class Config {
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }
}
