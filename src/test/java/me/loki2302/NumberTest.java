package me.loki2302;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NumberTest extends AbstractJedisTest {
    @Test
    public void canSetAndGetInt() {
        jedis.set("x", "1");
        assertEquals("1", jedis.get("x"));
    }
    
    @Test
    public void canIncreaseInt() {
        jedis.set("x", "1");
        jedis.incr("x");
        assertEquals("2", jedis.get("x"));
    }
    
    @Test
    public void canIncreaseIntBy() {
        jedis.set("x", "1");
        jedis.incrBy("x", 2);
        assertEquals("3", jedis.get("x"));
    }
    
    @Test
    public void canDecreaseInt() {
        jedis.set("x", "1");
        jedis.decr("x");
        assertEquals("0", jedis.get("x"));
    }
    
    @Test
    public void canDecreaseIntBy() {
        jedis.set("x", "1");
        jedis.decrBy("x", 2);
        assertEquals("-1", jedis.get("x"));
    }
}