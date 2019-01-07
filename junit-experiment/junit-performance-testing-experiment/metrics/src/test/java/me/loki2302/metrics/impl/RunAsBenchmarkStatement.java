package me.loki2302.metrics.impl;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RunAsBenchmarkStatement extends Statement {
    private final String className;
    private final String methodName;
    private final Object target;
    private final List<FrameworkMethod> beforeIterationMethods;
    private final List<FrameworkMethod> afterIterationMethods;
    private final Statement iterationStatement;
    private final Benchmark benchmark;

    public RunAsBenchmarkStatement(
            String className,
            String methodName,
            Object target,
            List<FrameworkMethod> beforeIterationMethods,
            List<FrameworkMethod> afterIterationMethods,
            Statement iterationStatement,
            Benchmark benchmark) {

        this.className = className;
        this.methodName = methodName;
        this.target = target;
        this.beforeIterationMethods = beforeIterationMethods;
        this.afterIterationMethods = afterIterationMethods;
        this.iterationStatement = iterationStatement;
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
            runBeforeIterationMethods();
            try {
                iterationStatement.evaluate();
            } finally {
                runAfterIterationMethods();
            }
        }

        logger.info("Running {} measurement iterations", benchmark.measure());
        for(int i = 0; i < benchmark.measure(); ++i) {
            runBeforeIterationMethods();
            try(Timer.Context context = timer.time()) {
                iterationStatement.evaluate();
            } finally {
                runAfterIterationMethods();
            }
        }

        String reportString;
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(baos)) {

            ConsoleReporter consoleReporter = ConsoleReporter
                    .forRegistry(metricRegistry)
                    .outputTo(printStream)
                    .build();
            consoleReporter.report();

            reportString = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        }

        Snapshot snapshot = timer.getSnapshot();
        double mean = snapshot.getMean() / TimeUnit.SECONDS.toNanos(1);
        String message = String.format("Mean execution time %f should be less than %f\n\n%s\n",
                mean,
                benchmark.expect(),
                reportString);
        logger.info(message);

        if(mean > benchmark.expect()) {
            throw new AssertionError(message);
        }
    }

    private void runBeforeIterationMethods() throws Throwable {
        for(FrameworkMethod method : beforeIterationMethods) {
            method.invokeExplosively(target);
        }
    }

    private void runAfterIterationMethods() throws Throwable {
        for(FrameworkMethod method : afterIterationMethods) {
            method.invokeExplosively(target);
        }
    }
}
