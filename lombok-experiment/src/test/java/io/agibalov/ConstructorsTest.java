package io.agibalov;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ConstructorsTest {
    @Test
    public void canConstructUserWithoutRequiredAttributesObjectUsingNoArgsConstructor() {
        UserWithoutRequiredAttributes user = new UserWithoutRequiredAttributes();
        assertNull(user.username);
        assertNull(user.password);
    }

    @Test
    public void canConstructUserWithoutRequiredAttributesObjectUsingAllArgsConstructor() {
        UserWithoutRequiredAttributes user = new UserWithoutRequiredAttributes(
                "someuser", "somepassword");
        assertEquals("someuser", user.username);
        assertEquals("somepassword", user.password);
    }

    @Test
    public void canConstructUserWithRequiredAttributesObjectUsingAllArgsConstructor() {
        UserWithRequiredAttributes user = new UserWithRequiredAttributes(
                "someuser", "somepassword");
        assertEquals("someuser", user.username);
        assertEquals("somepassword", user.password);
    }

    @Test
    public void canConstructUserWithRequiredAttributesObjectUsingRequiredArgsConstructor() {
        UserWithRequiredAttributes user = new UserWithRequiredAttributes("someuser");
        assertEquals("someuser", user.username);
        assertNull(user.password);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    private static class UserWithoutRequiredAttributes {
        private String username;
        private String password;
    }

    @RequiredArgsConstructor
    @AllArgsConstructor
    private static class UserWithRequiredAttributes {
        private final String username;
        private String password;
    }
}
