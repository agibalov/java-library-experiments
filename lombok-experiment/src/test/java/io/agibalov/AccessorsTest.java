package io.agibalov;

import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccessorsTest {
    @Test
    public void canUseAccessorsWithFluentFlag() {
        UserWithFluent user = new UserWithFluent()
                .username("someuser")
                .password("somepassword");
        assertEquals("someuser", user.username());
        assertEquals("somepassword", user.password());
    }

    @Test
    public void canUseAccessorsWithChainFlag() {
        UserWithChain user = new UserWithChain()
                .setUsername("someuser")
                .setPassword("somepassword");
        assertEquals("someuser", user.getUsername());
        assertEquals("somepassword", user.getPassword());
    }

    @Accessors(fluent = true)
    @Data
    private static class UserWithFluent {
        private String username;
        private String password;
    }

    @Accessors(chain = true)
    @Data
    private static class UserWithChain {
        private String username;
        private String password;
    }
}
