package me.loki2302;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class HashTest extends AbstractJedisTest {
    @Test
    public void canCreateHash() {
        jedis.hset("person", "name", "loki2302");
        jedis.hset("person", "age", "30");
        
        Map<String, String> fields = jedis.hgetAll("person");
        assertEquals("loki2302", fields.get("name"));
        assertEquals("30", fields.get("age"));
        
        assertEquals("loki2302", jedis.hget("person", "name"));
        assertEquals("30", jedis.hget("person", "age"));
        
        assertTrue(jedis.hexists("person", "name"));
        assertTrue(jedis.hexists("person", "age"));
        
        Set<String> keys = jedis.hkeys("person");
        assertTrue(keys.contains("name"));
        assertTrue(keys.contains("age"));
        
        List<String> values = jedis.hvals("person");
        assertTrue(values.contains("loki2302"));
        assertTrue(values.contains("30"));
    }
    
    @Test
    public void canDeleteField() {
        jedis.hset("person", "name", "loki2302");
        jedis.hset("person", "age", "30");
        jedis.hdel("person", "name");
        
        assertFalse(jedis.hexists("person", "name"));
        assertNull(jedis.hget("person", "name"));
        
        assertTrue(jedis.hexists("person", "age"));
        assertEquals("30", jedis.hget("person", "age"));
    }
        
    @Test
    @SuppressWarnings("serial")
    public void setMultipleFieldsAtOnce() {
        jedis.hmset("person", new HashMap<String, String>() {{
            put("name", "loki2302");
            put("age", "30");
        }});
        
        List<String> values = jedis.hmget("person", "name", "age");
        assertEquals("loki2302", values.get(0));
        assertEquals("30", values.get(1));
    }
}