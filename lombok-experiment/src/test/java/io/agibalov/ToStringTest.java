package io.agibalov;

import lombok.ToString;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ToStringTest {
    @Test
    public void canGenerateToStringForAClass() {
        MyClass myClass = new MyClass();
        assertEquals("ToStringTest.MyClass(a=hello, b=123)", myClass.toString());
    }

    @ToString
    private static class MyClass {
        private String a = "hello";
        private int b = 123;
    }
}
