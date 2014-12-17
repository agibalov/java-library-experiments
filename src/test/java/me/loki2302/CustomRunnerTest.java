package me.loki2302;

import org.junit.Test;
import org.junit.runner.*;
import org.junit.runner.notification.RunNotifier;

import static org.junit.Assert.assertEquals;

public class CustomRunnerTest {
    @Test
    public void canUseCustomRunner() {
        JUnitCore core = new JUnitCore();
        Result result = core.run(DummyTest.class);
        assertEquals(3, result.getRunCount());
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
            return Description.EMPTY; // ???
        }

        @Override
        public void run(RunNotifier notifier) {
            notifier.fireTestStarted(Description.EMPTY); // ???
            notifier.fireTestFinished(Description.EMPTY); // ???

            notifier.fireTestStarted(Description.EMPTY); // ???
            notifier.fireTestFinished(Description.EMPTY); // ???

            notifier.fireTestStarted(Description.EMPTY); // ???
            notifier.fireTestFinished(Description.EMPTY); // ???
        }
    }
}
