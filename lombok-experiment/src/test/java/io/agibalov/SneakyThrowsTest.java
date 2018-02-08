package io.agibalov;

import lombok.SneakyThrows;
import org.junit.Test;

import java.io.IOException;

public class SneakyThrowsTest {
    @Test(expected = IOException.class)
    public void canThrowCheckedExceptionsWithoutSpecifyingThemInMethodSignature() {
        someMethodThatThrowsIoException();
    }

    @SneakyThrows
    private static void someMethodThatThrowsIoException() {
        throw new IOException("I am IOException!");
    }
}
