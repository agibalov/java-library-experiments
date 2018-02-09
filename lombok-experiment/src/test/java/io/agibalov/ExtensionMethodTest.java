package io.agibalov;

import lombok.experimental.ExtensionMethod;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

// IDEA doesn't seem to support this - there are no suggestions, it shows the extension methods in red,
// but it compiles and works as expected.
@ExtensionMethod({
        ExtensionMethodTest.StringExtensions.class,
        ExtensionMethodTest.GlobalExtensions.class
})
public class ExtensionMethodTest {
    @Test
    public void canAddExtensionMethodToString() {
        String s = "hello world".appendExclamationMark();
        assertEquals("hello world!", s);
    }

    @Test
    public void canAddExtensionMethodToAllClasses() {
        List<Integer> list = Collections.singletonList(222);
        assertEquals("[222]!", list.toStringWithExclamationMark());
    }

    public static class StringExtensions {
        public static String appendExclamationMark(String object) {
            if(object == null) {
                return null;
            }
            return object + "!";
        }
    }

    public static class GlobalExtensions {
        public static <T> String toStringWithExclamationMark(T object) {
            return object.toString() + "!";
        }
    }
}
