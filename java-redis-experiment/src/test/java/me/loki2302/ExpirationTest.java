package me.loki2302;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExpirationTest extends AbstractJedisTest {
    @Test
    public void canUseExpiration() throws InterruptedException {
        jedis.set("x", "hello");
        jedis.expire("x", 1);
        assertEquals("hello", jedis.get("x"));
        Thread.sleep(1200);
        assertNull("hello", jedis.get("x"));
    }
    
    @Test
    public void canUseExpiration2() throws InterruptedException {
        jedis.psetex("x", 1000, "hello");
        assertEquals("hello", jedis.get("x"));
        Thread.sleep(1200);
        assertNull("hello", jedis.get("x"));
    }        
}