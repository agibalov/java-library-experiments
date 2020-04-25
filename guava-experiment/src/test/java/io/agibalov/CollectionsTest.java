package io.agibalov;

import com.google.common.collect.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CollectionsTest {
    @Test
    public void canUseMultiset() {
        Multiset<String> s = HashMultiset.create();
        s.add("loki2302");
        s.add("loki2302", 2);
        s.add("andrey");
        assertEquals(3, s.count("loki2302"));
        assertEquals(1, s.count("andrey"));
    }

    @Test
    public void canUseMultimap() {
        Multimap<String, String> m = ArrayListMultimap.create();
        m.put("loki2302", "hello");
        m.put("loki2302", "there");
        m.put("andrey", "wtf");
        assertEquals(3, m.size());
        assertEquals(2, m.keySet().size());
        assertEquals(2, m.get("loki2302").size());
        assertEquals(Arrays.asList("hello", "there"), m.get("loki2302"));
        assertEquals(1, m.get("andrey").size());
    }

    @Test
    public void canUseBiMap() {
        BiMap<String, Integer> m = HashBiMap.create();
        m.put("xxx", 111);
        m.put("yyy", 222);
        assertEquals(111, (int) m.get("xxx"));
        assertEquals(222, (int) m.get("yyy"));
        assertEquals("xxx", m.inverse().get(111));
        assertEquals("yyy", m.inverse().get(222));
    }
}
