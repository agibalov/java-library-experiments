package io.agibalov;

import lombok.experimental.Helper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelperTest {
    // NOTE: seems to be broken as of 1.16.20
    // "error: @Helper is legal only on method-local classes"
    /*@Test
    public void helperShouldWork() {
        String suffix = "!!!";

        @Helper class Dummy {
            String appendSuffix(String s) {
                return s + suffix;
            }
        }

        assertEquals("hello world!", appendSuffix("hello world"));
    }*/
}
