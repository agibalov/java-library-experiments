package me.loki2302;

import com.google.common.base.Joiner;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringsTest {
    @Test
    public void canUseJoiner() {
        String s = Joiner.on(", ").join("hello", "there", "omg");
        assertEquals("hello, there, omg", s);
    }
}
