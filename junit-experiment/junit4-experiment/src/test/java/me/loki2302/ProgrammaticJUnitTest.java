package me.loki2302;

import me.loki2302.shared.RecordingRunListener;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.junit.runners.model.InitializationError;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ProgrammaticJUnitTest {
    @Test
    public void canRunTestsProgrammatically() throws InitializationError {
        RecordingRunListener recordingRunListener = new RecordingRunListener();

        JUnitCore core = new JUnitCore();
        core.addListener(recordingRunListener);
        Result result = core.run(DummyTest.class);
        assertEquals(2, result.getRunCount());
        assertEquals(0, result.getFailureCount());
        assertTrue(result.wasSuccessful());

        List<String> events = recordingRunListener.events;
        assertEquals(6, events.size());
        assertEquals("testRunStarted null", events.get(0));
        assertEquals("testStarted oneAndTwoIsThree(me.loki2302.ProgrammaticJUnitTest$DummyTest)", events.get(1));
        assertEquals("testFinished oneAndTwoIsThree(me.loki2302.ProgrammaticJUnitTest$DummyTest)", events.get(2));
        assertEquals("testStarted twoAndThreeIsNotTen(me.loki2302.ProgrammaticJUnitTest$DummyTest)", events.get(3));
        assertEquals("testFinished twoAndThreeIsNotTen(me.loki2302.ProgrammaticJUnitTest$DummyTest)", events.get(4));
        assertEquals("testRunFinished true", events.get(5));
    }

    public static class DummyTest {
        @Test
        public void oneAndTwoIsThree() {
            assertEquals(3, 1 + 2);
        }

        @Test
        public void twoAndThreeIsNotTen() {
            assertNotEquals(10, 2 + 3);
        }
    }
}
