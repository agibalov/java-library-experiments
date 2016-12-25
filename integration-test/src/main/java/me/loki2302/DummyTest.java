package me.loki2302;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DummyTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(DummyTest.class);

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void canWriteToNodeAAndReadFromNodeA() throws InterruptedException {
        makeSureEverythingIsUp();

        restTemplate.put("http://172.25.0.11:8080/api/data", "hi there1");
        String s = restTemplate.getForObject("http://172.25.0.11:8080/api/data", String.class);
        assertEquals("hi there1", s);
    }

    @Test
    public void canWriteToNodeAAndReadFromNodeB() throws InterruptedException {
        makeSureEverythingIsUp();

        restTemplate.put("http://172.25.0.11:8080/api/data", "hi there2");
        String s = restTemplate.getForObject("http://172.25.0.22:8080/api/data", String.class);
        assertEquals("hi there2", s);
    }

    @Test
    public void canWriteToNodeBAndReadFromNodeA() throws InterruptedException {
        makeSureEverythingIsUp();

        restTemplate.put("http://172.25.0.22:8080/api/data", "hi there3");
        String s = restTemplate.getForObject("http://172.25.0.11:8080/api/data", String.class);
        assertEquals("hi there3", s);
    }

    private static void makeSureEverythingIsUp() throws InterruptedException {
        // TODO: what's the better way to make it wait?
        for(int i = 0; i < 20; ++i) {
            Thread.sleep(1000);
            LOGGER.info("Waiting {}", i);
        }
    }

    @Configuration
    public static class Config {
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }
}
