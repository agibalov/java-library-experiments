package me.loki2302;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ListTest extends AbstractJedisTest {
    @Test
    public void canPushRightItems() {
        jedis.rpush("x", "x1", "x2");
        List<String> items = jedis.lrange("x", 0, -1);
        assertEquals(2, items.size());
        assertEquals("x1", items.get(0));
        assertEquals("x2", items.get(1));
    }
    
    @Test
    public void canPopRightItems() {
        jedis.rpush("x", "x1", "x2");
        String x = jedis.rpop("x");
        assertEquals("x2", x);
        assertEquals(1, (long)jedis.llen("x"));
    }
}