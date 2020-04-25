package io.agibalov;

import org.junit.Test;

import static com.google.common.base.Preconditions.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PreconditionsTest {
    @Test
    public void dummy() {
        try {
            makeWelcomeMessage(null);
            fail();
        } catch (NullPointerException e) {
        }

        try {
            makeWelcomeMessage("");
            fail();
        } catch (IllegalArgumentException e) {
        }

        assertEquals("Hello, loki2302!", makeWelcomeMessage("loki2302"));
    }

    private static String makeWelcomeMessage(String name) {
        checkNotNull(name, "name should not be null");
        checkArgument(!name.isEmpty());
        return String.format("Hello, %s!", name);
    }
}
