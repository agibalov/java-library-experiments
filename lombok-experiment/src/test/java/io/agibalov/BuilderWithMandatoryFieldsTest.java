package io.agibalov;

import lombok.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class BuilderWithMandatoryFieldsTest {
    @Test
    public void builderThrowsIfMandatoryFieldIsNotSet() {
        try {
            User.builder().build();
            fail();
        } catch (NullPointerException e) {
            assertEquals("firstName is marked non-null but is null", e.getMessage());
        }

        try {
            User.builder().lastName("Smith").build();
            fail();
        } catch (NullPointerException e) {
            assertEquals("firstName is marked non-null but is null", e.getMessage());
        }

        User.builder().firstName("John").build();
        User.builder().firstName("John").lastName("Smith").build();
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        @NonNull
        private String firstName;

        private String lastName;
    }
}
