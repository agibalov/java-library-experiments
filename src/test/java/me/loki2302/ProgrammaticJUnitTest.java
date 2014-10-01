package me.loki2302;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runners.model.InitializationError;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ProgrammaticJUnitTest {
    @Test
    public void canRunTestsProgrammatically() throws InitializationError {
        JUnitCore core = new JUnitCore();
        Result result = core.run(DummyTest.class);
        assertEquals(2, result.getRunCount());
        assertEquals(0, result.getFailureCount());
        assertTrue(result.wasSuccessful());
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
