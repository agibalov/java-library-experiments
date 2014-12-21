package me.loki2302;

import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.*;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import static org.junit.Assert.assertEquals;

public class CustomRunnerTest {
    @Test
    public void canUseCustomRunner() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(DummyTest.class);

        // it's not quite clear what all these numbers mean
        // I interpret it like this:
        //   RunCount - tests which at least have been started (NOT ignored)
        //   FailureCount - tests which have been started but then failed
        //   IgnoreCount - tests which have been ignored
        // So, TotalTests = RunCount + IgnoreCount

        assertEquals(2, result.getRunCount());
        assertEquals(1, result.getFailureCount());
        assertEquals(1, result.getIgnoreCount());
    }

    @RunWith(DummyRunner.class)
    public static class DummyTest {
        @Test
        public void dummy() {
            // TODO
        }
    }

    public static class DummyRunner extends Runner {
        private final Class<?> testClass;

        public DummyRunner(Class<?> testClass) {
            this.testClass = testClass;
        }

        @Override
        public Description getDescription() {
            return Description.createSuiteDescription(testClass);
        }

        @Override
        public void run(RunNotifier notifier) {
            if(true) {
                Description description = Description.createTestDescription(
                        testClass,
                        "Test that succeeds");

                notifier.fireTestStarted(description);
                notifier.fireTestFinished(description);
            }

            if(true) {
                Description description = Description.createTestDescription(
                        testClass,
                        "Test that fails");

                notifier.fireTestStarted(description);

                Failure failure = new Failure(
                        description,
                        new ComparisonFailure(
                                "Some sort of comparison failure",
                                "loki2302",
                                "10k12302"));
                notifier.fireTestFailure(failure);
                notifier.fireTestFinished(description);
            }

            if(true) {
                Description description = Description.createTestDescription(
                        testClass,
                        "An ignored test");

                notifier.fireTestIgnored(description);
            }
        }
    }
}
