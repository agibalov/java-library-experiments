package me.loki2302;

import org.junit.After;
import org.junit.Before;

import redis.clients.jedis.Jedis;
import redis.embedded.RedisServer;

import java.io.IOException;

public abstract class AbstractJedisTest {
    private RedisServer redisServer;
    protected Jedis jedis;
    
    @Before
    public void startRedis() throws IOException {
        redisServer = new RedisServer();
        redisServer.start();

        jedis = new Jedis("localhost");
        jedis.flushAll();
    }

    @After
    public void stopRedis() {
        redisServer.stop();
    }
}
