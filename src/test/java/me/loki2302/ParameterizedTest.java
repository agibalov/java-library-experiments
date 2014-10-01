package me.loki2302;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ParameterizedTest {
    private final int a;
    private final int b;
    private final int expectedResult;

    public ParameterizedTest(int a, int b, int expectedResult) {
        this.a = a;
        this.b = b;
        this.expectedResult = expectedResult;
    }

    @Test
    public void aAndBShouldEqualExpectedResult() {
        int actualResult = a + b;
        assertEquals(expectedResult, actualResult);
    }

    @Parameterized.Parameters(name = "{0} + {1} is {2}")
    public static Collection<Object[]> testCases() {
        return Arrays.asList(
                new Object[] {1, 2, 3},
                new Object[] {2, 3, 5}
        );
    }
}
