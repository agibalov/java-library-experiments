package io.agibalov;

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GetterSetterTest {
    @Test
    public void canUseGetter() {
        SomethingWithGetter somethingWithGetter = new SomethingWithGetter();
        somethingWithGetter.something = "hello";
        assertEquals("hello", somethingWithGetter.getSomething());
    }

    @Test
    public void canUseSetter() {
        SomethingWithSetter somethingWithSetter = new SomethingWithSetter();
        somethingWithSetter.setSomething("hello");
        assertEquals("hello", somethingWithSetter.something);
    }

    @Getter
    private static class SomethingWithGetter {
        private String something;
    }

    @Setter
    private static class SomethingWithSetter {
        private String something;
    }
}
