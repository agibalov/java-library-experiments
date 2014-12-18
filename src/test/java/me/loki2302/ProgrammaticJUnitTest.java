package me.loki2302;

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

        assertEquals("testRunStarted", recordingRunListener.events.get(0));
        assertEquals("testStarted", recordingRunListener.events.get(1));
        assertEquals("testFinished", recordingRunListener.events.get(2));
        assertEquals("testStarted", recordingRunListener.events.get(3));
        assertEquals("testFinished", recordingRunListener.events.get(4));
        assertEquals("testRunFinished", recordingRunListener.events.get(5));
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

    public static class RecordingRunListener extends RunListener {
        public List<String> events = new ArrayList<String>();

        @Override
        public void testRunStarted(Description description) throws Exception {
            events.add("testRunStarted");
        }

        @Override
        public void testRunFinished(Result result) throws Exception {
            events.add("testRunFinished");
        }

        @Override
        public void testStarted(Description description) throws Exception {
            events.add("testStarted");
        }

        @Override
        public void testFinished(Description description) throws Exception {
            events.add("testFinished");
        }
    }
}
