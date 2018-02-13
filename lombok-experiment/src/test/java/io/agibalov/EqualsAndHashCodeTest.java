package io.agibalov;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EqualsAndHashCodeTest {
    @Test
    public void canGenerateEqualsAndHashCode() {
        assertEquals(new User("aaa", "bbb"), new User("aaa", "bbb"));
        assertNotEquals(new User("aaa1", "bbb1"), new User("aaa2", "bbb2"));
    }

    @EqualsAndHashCode
    @Data
    @AllArgsConstructor
    private static class User {
        private String username;
        private String password;
    }
}
