package me.loki2302;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runners.model.Statement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RulesTest {
    @Test
    public void canUseTemporaryFolderRule() {
        JUnitCore jUnitCore = new JUnitCore();
        Result result = jUnitCore.run(DummyTestThatUsesTemporaryFolderRule.class);
        assertEquals(1, result.getRunCount());
        assertTrue(result.wasSuccessful());
        assertFalse(DummyTestThatUsesTemporaryFolderRule.temporaryFolderRoot.exists());
    }

    public static class DummyTestThatUsesTemporaryFolderRule {
        public static File temporaryFolderRoot;

        @Rule
        public TemporaryFolder temporaryFolder = new TemporaryFolder();

        @Test
        public void canUseTemporaryFolderRule() throws IOException {
            temporaryFolderRoot = temporaryFolder.getRoot();
            assertTrue(temporaryFolder.getRoot().exists());

            File file = temporaryFolder.newFile();
            assertTrue(file.exists());

            File folder = temporaryFolder.newFolder();
            assertTrue(folder.exists());

            File fileWithinAFolder = Paths.get(folder.getPath(), "1.txt").toFile();
            fileWithinAFolder.createNewFile();
            assertTrue(fileWithinAFolder.exists());
        }
    }

    @Test
    public void canUseExceptionCounterRule() {
        JUnitCore jUnitCore = new JUnitCore();
        Result result = jUnitCore.run(DummyTestThatUsesExceptionCounterRule.class);
        assertEquals(3, result.getRunCount());
        assertFalse(result.wasSuccessful());
        assertEquals(2, ExceptionCounterRule.exceptionCount);
    }

    public static class DummyTestThatUsesExceptionCounterRule {
        @Rule
        public ExceptionCounterRule exceptionCounterRule = new ExceptionCounterRule();

        @Test
        public void failingTest1() {
            assertTrue(false);
        }

        @Test
        public void succeedingTest1() {
            assertTrue(true);
        }

        @Test
        public void failingTest2() {
            assertTrue(false);
        }
    }

    public static class ExceptionCounterRule implements TestRule {
        public static int exceptionCount;

        @Override
        public Statement apply(final Statement base, Description description) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    try {
                        base.evaluate();
                    } catch (Throwable throwable) {
                        ++exceptionCount;
                        throw throwable;
                    }
                }
            };
        }
    }
}
