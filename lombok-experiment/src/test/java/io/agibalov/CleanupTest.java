package io.agibalov;

import lombok.Cleanup;
import org.junit.Test;

import java.io.Closeable;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class CleanupTest {
    @Test
    public void canUseCleanup() throws IOException {
        MyCloseable.isClosed = false;
        {
            // gets closed when before exiting the scope
            @Cleanup MyCloseable myCloseable = new MyCloseable();
        }
        assertTrue(MyCloseable.isClosed);
    }

    private static class MyCloseable implements Closeable {
        private static boolean isClosed;

        @Override
        public void close() throws IOException {
            isClosed = true;
        }
    }
}
