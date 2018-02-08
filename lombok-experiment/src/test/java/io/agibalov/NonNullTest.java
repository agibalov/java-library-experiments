package io.agibalov;

import lombok.Data;
import lombok.NonNull;
import org.junit.Test;

public class NonNullTest {
    @Test(expected = NullPointerException.class)
    public void canMakeMethodsThrowIfArgumentIsNull() {
        processTheString(null);
    }

    @Test(expected = NullPointerException.class)
    public void canMakeConstructorThrowIfArgumentIsNull() {
        new SomeClass(null);
    }

    @Test(expected = NullPointerException.class)
    public void canMakeDataConstructorThrowIfArgumentIsNull() {
        new SomeDataClass(null);
    }

    @Test(expected = NullPointerException.class)
    public void canMakeDataSetterThrowIfArgumentIsNull() {
        SomeDataClass someDataClass = new SomeDataClass("hello");
        someDataClass.setS(null);
    }

    private static void processTheString(@NonNull String s) {
        // NOTE: we don't use s here
    }

    private static class SomeClass {
        public SomeClass(@NonNull String s) {
        }
    }

    @Data
    private static class SomeDataClass {
        @NonNull
        private String s;
    }
}
