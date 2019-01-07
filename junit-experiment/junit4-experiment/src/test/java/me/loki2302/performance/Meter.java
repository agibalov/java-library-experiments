package me.loki2302.performance;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.TimeUnit;

public class Meter implements TestRule {
    private TestContext testContext;

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                String className = description.getClassName();
                String methodName = description.getMethodName();
                testContext = new TestContext(className, methodName);
                try {
                    base.evaluate();
                } finally {
                    testContext = null;
                }
            }
        };
    }

    public Measurement measure(String name, int repetitions, TestFunc testFunc) {
        if(testContext == null) {
            throw new IllegalStateException();
        }

        MetricRegistry metricRegistry = new MetricRegistry();

        String timerName = MetricRegistry.name(testContext.className, testContext.methodName, name);
        Timer timer = metricRegistry.timer(timerName);
        for(int i = 0; i < repetitions; ++i) {
            try(Timer.Context context = timer.time()) {
                testFunc.run();
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }

        Snapshot snapshot = timer.getSnapshot();
        Measurement measurement = new SimpleMeasurement(
                snapshot.getMin() / TimeUnit.MILLISECONDS.toNanos(1),
                snapshot.getMax() / TimeUnit.MILLISECONDS.toNanos(1),
                (long)snapshot.getMean() / TimeUnit.MILLISECONDS.toNanos(1),
                (long)snapshot.getMedian() / TimeUnit.MILLISECONDS.toNanos(1));

        return measurement;
    }

    private static class TestContext {
        public final String className;
        public final String methodName;

        private TestContext(String className, String methodName) {
            this.className = className;
            this.methodName = methodName;
        }
    }

    private static class SimpleMeasurement implements Measurement {
        private final long min;
        private final long max;
        private final long mean;
        private final long median;

        private SimpleMeasurement(long min, long max, long mean, long median) {
            this.min = min;
            this.max = max;
            this.mean = mean;
            this.median = median;
        }

        @Override
        public long getMin() {
            return min;
        }

        @Override
        public long getMax() {
            return max;
        }

        @Override
        public long getMean() {
            return mean;
        }

        @Override
        public long getMedian() {
            return median;
        }
    }
}
