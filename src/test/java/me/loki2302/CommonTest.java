package me.loki2302;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.junit.Test;

public class CommonTest extends AbstractJedisTest {
    @Test
    public void defaultsAreAdequate() {
        assertNull(jedis.get("x"));
        assertEquals(0L, (long)jedis.dbSize());
        assertFalse(jedis.exists("x"));
    }
    
    @Test
    public void canSetKey() {
        jedis.set("x", "hello");
        assertEquals("hello", jedis.get("x"));
    }
    
    @Test
    public void canDeleteKey() {
        jedis.set("x", "hello");
        jedis.del("x");
        assertNull("hello", jedis.get("x"));
    }    
    
    @Test
    public void canGetKeysByPattern() {
        jedis.set("x1", "hello");
        jedis.set("x2", "hello");
        Set<String> keys = jedis.keys("x*");
        assertTrue(keys.contains("x1"));
        assertTrue(keys.contains("x2"));
    }
    
    @Test
    public void canSetMultipleValues() {
        jedis.mset("x", "1", "y", "2");
        assertEquals("1", jedis.get("x"));
        assertEquals("2", jedis.get("y"));
    }
    
    @Test
    public void canGetValuesByMultipleKeys() {
        jedis.set("x", "1");
        jedis.set("y", "2");
        List<String> values = jedis.mget("x", "y");
        assertTrue(values.contains("1"));
        assertTrue(values.contains("2"));
    }
    
    @Test
    public void canSetValueIfNotSetYet() {
        jedis.setnx("x", "1");
        assertEquals("1", jedis.get("x"));
        jedis.setnx("x", "2");
        assertEquals("1", jedis.get("x"));
    }
    
    @Test
    public void canRenameKey() {
        jedis.set("x", "1");
        jedis.rename("x", "y");
        assertFalse(jedis.exists("x"));
        assertEquals("1", jedis.get("y"));
    }
}