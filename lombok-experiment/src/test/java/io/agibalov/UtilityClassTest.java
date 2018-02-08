package io.agibalov;

import lombok.experimental.UtilityClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilityClassTest {
    @Test
    public void canUseSomeMathAsStaticClass() {
        // NOTE: magically becomes static
        assertEquals(5, SomeMath.addNumbers(2, 3));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void cantInstantiateSomeMath() {
        new SomeMath();
    }

    @UtilityClass
    private static class SomeMath {
        public int addNumbers(int a, int b) {
            return a + b;
        }
    }
}
