package me.loki2302;

import me.loki2302.customrunner.MyIgnore;
import me.loki2302.customrunner.MyRunner;
import me.loki2302.customrunner.MyTest;
import me.loki2302.shared.RecordingRunListener;
import org.junit.Test;
import org.junit.runner.*;

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

    @RunWith(MyRunner.class)
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
}
