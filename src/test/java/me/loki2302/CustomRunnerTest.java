package me.loki2302;

import me.loki2302.shared.RecordingRunListener;
import org.junit.Test;
import org.junit.runner.*;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CustomRunnerTest {
    @Test
    public void canUseCustomRunner() {
        RecordingRunListener recordingRunListener = new RecordingRunListener();

        JUnitCore core = new JUnitCore();
        core.addListener(recordingRunListener);
        Result result = core.run(DummyTest.class);

        assertEquals(2, result.getRunCount());
        assertEquals(1, result.getFailureCount());
        assertEquals(1, result.getIgnoreCount());

        List<String> events = recordingRunListener.events;
        assertEquals(8, events.size());
        assertEquals("testRunStarted null", events.get(0));
        assertEquals("testStarted badTest(me.loki2302.CustomRunnerTest$DummyTest)", events.get(1));
        assertEquals("testFailure null", events.get(2));
        assertEquals("testFinished badTest(me.loki2302.CustomRunnerTest$DummyTest)", events.get(3));
        assertEquals("testIgnored ignoredTest(me.loki2302.CustomRunnerTest$DummyTest)", events.get(4));
        assertEquals("testStarted goodTest(me.loki2302.CustomRunnerTest$DummyTest)", events.get(5));
        assertEquals("testFinished goodTest(me.loki2302.CustomRunnerTest$DummyTest)", events.get(6));
        assertEquals("testRunFinished false", events.get(7));
    }

    @RunWith(DummyRunner.class)
    public static class DummyTest {
        @MyTest
        public void goodTest() {
        }

        @MyTest
        public void badTest() {
            assertTrue(false);
        }

        @MyIgnore
        @MyTest
        public void ignoredTest() {
        }
    }

    public static class DummyRunner extends Runner {
        private final Class<?> targetClass;

        public DummyRunner(Class<?> targetClass) {
            this.targetClass = targetClass;
        }

        @Override
        public Description getDescription() {
            return Description.createSuiteDescription(targetClass);
        }

        @Override
        public void run(RunNotifier notifier) {
            TestClass testClass = new TestClass(targetClass);
            Object testObject;
            try {
                testObject = targetClass.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            List<FrameworkMethod> testMethods = testClass.getAnnotatedMethods(MyTest.class);
            for (FrameworkMethod testMethod : testMethods) {
                Description testDescription = Description.createTestDescription(targetClass, testMethod.getName());

                MyIgnore myIgnore = testMethod.getAnnotation(MyIgnore.class);
                if(myIgnore != null) {
                    notifier.fireTestIgnored(testDescription);
                    continue;
                }

                notifier.fireTestStarted(testDescription);
                try {
                    testMethod.invokeExplosively(testObject);
                } catch (Throwable throwable) {
                    notifier.fireTestFailure(new Failure(testDescription, throwable));
                } finally {
                    notifier.fireTestFinished(testDescription);
                }
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface MyTest {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface MyIgnore {
    }
}
