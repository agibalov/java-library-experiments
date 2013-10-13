package me.loki2302;

import org.junit.Before;

import redis.clients.jedis.Jedis;

public abstract class AbstractJedisTest {
    protected Jedis jedis;
    
    @Before
    public void setUpJedis() {
        jedis = new Jedis("192.168.1.99");
        jedis.flushAll();
    }
}