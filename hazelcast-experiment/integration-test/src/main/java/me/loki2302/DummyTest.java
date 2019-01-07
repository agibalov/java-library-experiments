package me.loki2302;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DummyTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(DummyTest.class);

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void canWriteToNodeAAndReadFromNodeA() {
        ping("http://172.25.0.11:8080/info");

        restTemplate.put("http://172.25.0.11:8080/api/data", "hi there1");
        String s = restTemplate.getForObject("http://172.25.0.11:8080/api/data", String.class);
        assertEquals("hi there1", s);
    }

    @Test
    public void canWriteToNodeAAndReadFromNodeB() throws InterruptedException {
        ping("http://172.25.0.11:8080/info");
        ping("http://172.25.0.22:8080/info");

        restTemplate.put("http://172.25.0.11:8080/api/data", "hi there2");
        String s = restTemplate.getForObject("http://172.25.0.22:8080/api/data", String.class);
        assertEquals("hi there2", s);
    }

    @Test
    public void canWriteToNodeBAndReadFromNodeA() throws InterruptedException {
        ping("http://172.25.0.11:8080/info");
        ping("http://172.25.0.22:8080/info");

        restTemplate.put("http://172.25.0.22:8080/api/data", "hi there3");
        String s = restTemplate.getForObject("http://172.25.0.11:8080/api/data", String.class);
        assertEquals("hi there3", s);
    }

    private static void ping(String url) {
        RestTemplate restTemplate = new RestTemplate();

        for(int i = 1; i <= 100; ++i) {
            ResponseEntity<String> responseEntity = null;
            ResourceAccessException resourceAccessException = null;
            try {
                responseEntity = restTemplate.getForEntity(url, String.class);
            } catch (ResourceAccessException e) {
                resourceAccessException = e;
            }

            if(resourceAccessException != null) {
                LOGGER.info("Pinging {} - attempt #{} failed ({})", url, i, resourceAccessException.getMessage());
            } else if(!responseEntity.getStatusCode().is2xxSuccessful()) {
                LOGGER.info("Pinging {} - attempt #{} failed ({})", url, i, responseEntity.getStatusCode());
            } else {
                LOGGER.info("Pinging {} - attempt #{} succeeded", url, i);
                break;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {}
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
