package me.loki2302.metrics.impl;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

/**
 * A specialization of {@link BlockJUnit4ClassRunner} that allows running tests in 2 modes:
 * "tests only" and "benchmarks". The mode is determined based on system property ENABLE_BENCHMARK -
 * as long as it's not set or its value is empty, tests are run in "tests only" mode. Otherwise they
 * are run in "benchmarks" mode.
 *
 * In "tests only" mode, the execution flow is like this:
 *
 * For every test method...
 * <ul>
 *     <li>@Before methods x 1</li>
 *     <li>@BeforeIteration methods x 1</li>
 *     <li>@Test method itself x 1</li>
 *     <li>@AfterIteration methods x 1</li>
 *     <li>@After methods x 1</li>
 * </ul>
 *
 * In "benchmarks" mode, the execution flow is like this:
 *
 * For every test method...
 * <ul>
 *     <li>@Before methods x 1</li>
 *     <li>@BeforeIteration methods x (WARMUP_IT + MEASUREMENT_IT)</li>
 *     <li>@Test method itself x (WARMUP_IT + MEASUREMENT_IT)</li>
 *     <li>@AfterIteration methods x (WARMUP_IT + MEASUREMENT_IT)</li>
 *     <li>@After methods x 1</li>
 * </ul>
 *
 * @see AfterIteration
 * @see BeforeIteration
 * @see Benchmark
 */
public class FancyRunner extends BlockJUnit4ClassRunner {
    private final static String ENABLE_BENCHMARK_PROPERTY_NAME = "ENABLE_BENCHMARK";

    public FancyRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        // test method itself
        Statement statement = super.methodInvoker(method, test);

        statement = withBeforeIterationMethods(statement, test);
        statement = withAfterIterationMethods(statement, test);

        if(!System.getProperty(ENABLE_BENCHMARK_PROPERTY_NAME, "").equals("")) {
            statement = withBenchmark(statement, method);
        }

        return statement;
    }

    private Statement withBeforeIterationMethods(Statement statement, Object test) {
        List<FrameworkMethod> beforeFrameworkMethods = getTestClass().getAnnotatedMethods(BeforeIteration.class);
        return beforeFrameworkMethods.isEmpty() ? statement : new RunBefores(statement, beforeFrameworkMethods, test);
    }

    private Statement withAfterIterationMethods(Statement statement, Object test) {
        List<FrameworkMethod> afterFrameworkMethods = getTestClass().getAnnotatedMethods(AfterIteration.class);
        return afterFrameworkMethods.isEmpty() ? statement : new RunAfters(statement, afterFrameworkMethods, test);
    }

    private Statement withBenchmark(
            Statement statement,
            FrameworkMethod method) {

        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();

        Benchmark benchmark = method.getAnnotation(Benchmark.class);
        return benchmark == null ? statement : new RunBenchmarkStatement(className, methodName, statement, benchmark);
    }

    private static class RunBenchmarkStatement extends Statement {
        private final String className;
        private final String methodName;
        private final Statement next;
        private final Benchmark benchmark;

        private RunBenchmarkStatement(
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
}
