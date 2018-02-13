package io.agibalov;

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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

    @Test
    public void canUseOnMethod() throws NoSuchMethodException {
        HeyThere heyThere = SomethingWithFieldLevelGetter.class
                .getDeclaredMethod("getSomething")
                .getAnnotation(HeyThere.class);
        assertEquals("how are you?", heyThere.value());
    }

    @Getter
    private static class SomethingWithGetter {
        private String something;
    }

    @Setter
    private static class SomethingWithSetter {
        private String something;
    }

    private static class SomethingWithFieldLevelGetter {
        @Getter(onMethod = @__(@HeyThere("how are you?")))
        private String something;
    }

    @Retention(RetentionPolicy.RUNTIME)
    private @interface HeyThere {
        String value();
    }
}
