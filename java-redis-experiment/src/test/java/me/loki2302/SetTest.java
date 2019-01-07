package me.loki2302;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class SetTest extends AbstractJedisTest {
    @Test
    public void canCreateSet() {
        jedis.sadd("x", "loki2302", "john smith");
        assertEquals(2, (long)jedis.scard("x"));
        
        jedis.sadd("x", "loki2302");
        assertEquals(2, (long)jedis.scard("x"));
        
        Set<String> items = jedis.smembers("x");
        assertEquals(2, items.size());
        assertTrue(items.contains("loki2302"));
        assertTrue(items.contains("john smith"));
    }
    
    @Test
    public void canRemoveItemFromSet() {
        jedis.sadd("x", "loki2302", "john smith");
        jedis.srem("x", "loki2302");
        assertEquals(1, (long)jedis.scard("x"));
    }
    
    @Test
    public void canGetDifference() {
        jedis.sadd("x", "loki2302", "john smith");
        jedis.sadd("y", "john smith", "qwerty");
        
        Set<String> difference1 = jedis.sdiff("x", "y");
        assertEquals(1, difference1.size());
        assertTrue(difference1.contains("loki2302"));
        
        Set<String> difference2 = jedis.sdiff("y", "x");
        assertEquals(1, difference2.size());
        assertTrue(difference2.contains("qwerty"));
    }
    
    @Test
    public void canGetIntersection() {
        jedis.sadd("x", "loki2302", "john smith");
        jedis.sadd("y", "john smith", "qwerty");
        
        Set<String> intersection = jedis.sinter("x", "y");
        assertEquals(1, intersection.size());
        assertTrue(intersection.contains("john smith"));
    }
    
    @Test
    public void canGetUnion() {
        jedis.sadd("x", "loki2302", "john smith");
        jedis.sadd("y", "john smith", "qwerty");
        
        Set<String> union = jedis.sunion("x", "y");
        assertEquals(3, union.size());
        assertTrue(union.contains("loki2302"));
        assertTrue(union.contains("john smith"));
        assertTrue(union.contains("qwerty"));
    }
}