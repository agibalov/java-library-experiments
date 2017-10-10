package me.loki2302;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class DummyTest {
    private ExecutorService es;

    @Before
    public void startExecutorService() {
        es = Executors.newFixedThreadPool(1);
    }

    @After
    public void stopExecutorService() {
        es.shutdown();
    }

    @Test
    public void itShouldWork() {
        Mono<String> data = getData();
        String s = data.block();
        assertEquals("hello", s);
    }

    private Mono<String> getData() {
        return Mono.create(sink -> {
            es.submit(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sink.success("hello");
            });
        });
    }
}
