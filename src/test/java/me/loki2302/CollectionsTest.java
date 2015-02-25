package me.loki2302;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.junit.Test;

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
}
