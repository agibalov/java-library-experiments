package me.loki2302;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringTest extends AbstractJedisTest {
    @Test
    public void canSetAndGetString() {
        jedis.set("x", "hello");
        assertEquals("hello", jedis.get("x"));
    }
    
    @Test
    public void canAppendString() {
        jedis.set("x", "hello");
        jedis.append("x", "world");
        assertEquals("helloworld", jedis.get("x"));
    }
    
    @Test
    public void canGetSubstring() {
        jedis.set("x", "hello");
        assertEquals("ell", jedis.getrange("x", 1, 3));        
    }
    
    @Test
    public void canReplaceString() {
        jedis.set("x", "hello");
        assertEquals("hello", jedis.getSet("x", "world"));
        assertEquals("world", jedis.get("x"));
    } 
    
    @Test
    public void canGetStringLength() {
        jedis.set("x", "hello");
        assertEquals(5, (long)jedis.strlen("x"));
    }
}
