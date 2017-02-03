package me.loki2302.metrics.impl;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class RunBenchmarkStatement extends Statement {
    private final String className;
    private final String methodName;
    private final Statement next;
    private final Benchmark benchmark;

    public RunBenchmarkStatement(
            String className,
            String methodName,
            Statement next,
            Benchmark benchmark) {

        this.className = className;
        this.methodName = methodName;
        this.next = next;
        this.benchmark = benchmark;
    }

    @Override
    public void evaluate() throws Throwable {
        MetricRegistry metricRegistry = new MetricRegistry();
        String timerName = MetricRegistry.name(className, methodName);
        Timer timer = metricRegistry.timer(timerName);

        Logger logger = LoggerFactory.getLogger(timerName);

        logger.info("Running {} warmup iterations", benchmark.warmup());
        for(int i = 0; i < benchmark.warmup(); ++i) {
            next.evaluate();
        }

        logger.info("Running {} measurement iterations", benchmark.measure());
        for(int i = 0; i < benchmark.measure(); ++i) {
            try(Timer.Context context = timer.time()) {
                next.evaluate();
            }
        }

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(baos)) {

            ConsoleReporter consoleReporter = ConsoleReporter
                    .forRegistry(metricRegistry)
                    .outputTo(printStream)
                    .build();
            consoleReporter.report();

            logger.info("Results\n{}", new String(baos.toByteArray(), StandardCharsets.UTF_8));
        }

        Snapshot snapshot = timer.getSnapshot();
        double mean = snapshot.getMean() / TimeUnit.SECONDS.toNanos(1);
        logger.info("Mean execution time {} should be less than {}", mean, benchmark.expect());

        assertThat(mean, lessThanOrEqualTo(benchmark.expect()));
    }
}
