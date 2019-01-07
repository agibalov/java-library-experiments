package me.loki2302;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class OrderedSetTest extends AbstractJedisTest {
    @Test
    public void canCreateOrderedSet() {
        jedis.zadd("x", 1.0, "item #1");
        jedis.zadd("x", 2.0, "item #2");
        assertEquals(2, (long)jedis.zcard("x"));
        Set<String> items = jedis.zrange("x", 0, -1);
        assertEquals(2, items.size());
        assertTrue(items.contains("item #1"));
        assertTrue(items.contains("item #2"));
    }
}