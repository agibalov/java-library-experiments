package io.agibalov;

import lombok.Builder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BuilderTest {
    @Test
    public void canUseBuilder() {
        User user = User.builder()
                .username("qwerty")
                .password("asdfgh")
                .build();
        assertEquals("qwerty", user.username);
        assertEquals("asdfgh", user.password);
    }

    @Builder
    private static class User {
        private String username;
        private String password;
    }
}
