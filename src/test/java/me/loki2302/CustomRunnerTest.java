package me.loki2302;

import me.loki2302.shared.RecordingRunListener;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.*;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CustomRunnerTest {
    @Test
    public void canUseCustomRunner() {
        RecordingRunListener recordingRunListener = new RecordingRunListener();

        JUnitCore core = new JUnitCore();
        core.addListener(recordingRunListener);
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

        List<String> events = recordingRunListener.events;
        assertEquals(8, events.size());
        assertEquals("testRunStarted null", events.get(0));
        assertEquals("testStarted Test that succeeds(me.loki2302.CustomRunnerTest$DummyTest)", events.get(1));
        assertEquals("testFinished Test that succeeds(me.loki2302.CustomRunnerTest$DummyTest)", events.get(2));
        assertEquals("testStarted Test that fails(me.loki2302.CustomRunnerTest$DummyTest)", events.get(3));
        assertEquals("testFailure Some sort of comparison failure expected:<[loki]2302> but was:<[10k1]2302>", events.get(4));
        assertEquals("testFinished Test that fails(me.loki2302.CustomRunnerTest$DummyTest)", events.get(5));
        assertEquals("testIgnored An ignored test(me.loki2302.CustomRunnerTest$DummyTest)", events.get(6));
        assertEquals("testRunFinished false", events.get(7));
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
