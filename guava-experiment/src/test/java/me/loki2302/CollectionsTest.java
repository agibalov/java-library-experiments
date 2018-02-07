package me.loki2302;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import org.junit.Test;

import java.util.Arrays;

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
}
