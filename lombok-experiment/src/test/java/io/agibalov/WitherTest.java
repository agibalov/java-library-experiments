package io.agibalov;

import lombok.AllArgsConstructor;
import lombok.experimental.Wither;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WitherTest {
    @Test
    public void canGenerateAWithMethod() {
        User user = new User("jsmith", "qwerty");
        User updatedUser = user.withPassword("123456");
        assertEquals("qwerty", user.password);
        assertEquals("jsmith", updatedUser.username);
        assertEquals("123456", updatedUser.password);
    }

    @AllArgsConstructor
    private static class User {
        private String username;

        @Wither
        private String password;
    }
}
