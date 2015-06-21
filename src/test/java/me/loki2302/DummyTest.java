package me.loki2302;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DummyTest {
    private CacheManager cacheManager;
    private Cache testCache;

    @Before
    public void startEhcache() {
        cacheManager = CacheManager.newInstance();
        cacheManager.addCache("test");

        testCache = cacheManager.getCache("test");
    }

    @After
    public void stopEhcache() {
        cacheManager.shutdown();
    }

    @Test
    public void canPutAndGetElement() {
        testCache.put(new Element("x", 123));
        Element xElement = testCache.get("x");
        assertEquals(123, (int)(Integer)xElement.getObjectValue());
    }

    @Test
    public void canSetElementTimeToLive() throws InterruptedException {
        {
            Element element = new Element("x", 123);
            element.setTimeToIdle(1);
            testCache.put(element);
        }

        assertTrue(testCache.getKeysWithExpiryCheck().contains("x"));

        Thread.sleep(1100);

        assertFalse(testCache.getKeysWithExpiryCheck().contains("x"));
    }
}
