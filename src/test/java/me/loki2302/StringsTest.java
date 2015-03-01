package me.loki2302;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class StringsTest {
    @Test
    public void canUseJoiner() {
        String s = Joiner.on(", ").join("hello", "there", "omg");
        assertEquals("hello, there, omg", s);
    }

    @Test
    public void canUseSplitter() {
        List<String> terms = Lists.newArrayList(Splitter.on(", ").split("hello, there, omg"));
        assertEquals(3, terms.size());
        assertEquals("hello", terms.get(0));
        assertEquals("there", terms.get(1));
        assertEquals("omg", terms.get(2));
    }
}
