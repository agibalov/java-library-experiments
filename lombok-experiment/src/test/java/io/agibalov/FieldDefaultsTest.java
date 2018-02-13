package io.agibalov;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.junit.Test;

import java.lang.reflect.Modifier;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FieldDefaultsTest {
    @Test
    public void canSetDefaultAccessLevel() throws NoSuchFieldException {
        int usernameFieldModifier = User.class.getDeclaredField("username").getModifiers();
        assertTrue(Modifier.isPrivate(usernameFieldModifier));
        assertTrue(Modifier.isFinal(usernameFieldModifier));

        int passwordFieldModifiers = User.class.getDeclaredField("password").getModifiers();
        assertTrue(Modifier.isPublic(passwordFieldModifiers));
        assertFalse(Modifier.isFinal(passwordFieldModifiers));
    }

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @RequiredArgsConstructor
    private static class User {
        String username;
        @NonFinal public String password;
    }
}
