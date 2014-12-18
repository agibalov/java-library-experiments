package me.loki2302;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RulesTest {
    @Test
    public void canUseTemporaryFolderRule() {
        JUnitCore jUnitCore = new JUnitCore();
        Result result = jUnitCore.run(DummyTest.class);
        assertEquals(1, result.getRunCount());
        assertTrue(result.wasSuccessful());
        assertFalse(DummyTest.temporaryFolderRoot.exists());
    }

    public static class DummyTest {
        public static File temporaryFolderRoot;

        @Rule
        public TemporaryFolder temporaryFolder = new TemporaryFolder();

        @Test
        public void canUseTemporaryFolderRule() throws IOException {
            assertTrue(temporaryFolder.getRoot().exists());
            temporaryFolderRoot = temporaryFolder.getRoot();
        }
    }
}
