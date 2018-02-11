package io.agibalov;

import lombok.Value;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ValueTest {
    @Test
    public void canUseAValueObject() {
        User user = new User("qwerty", "asdfgh");
        assertEquals("qwerty", user.getUsername());
        assertEquals("asdfgh", user.getPassword());
        // NOTE: there are no setters
    }

    @Value
    private static class User {
        private String username;
        private String password;
    }
}
