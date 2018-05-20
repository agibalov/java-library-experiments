package io.agibalov;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class NullnessDemo {
    public void doSomething() {
        @NonNull String s = getString();
    }

    @Nullable
    private static String getString() {
        return "hello";
    }
}
