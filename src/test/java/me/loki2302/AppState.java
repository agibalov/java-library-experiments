package me.loki2302;

import org.openjdk.jmh.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestTemplate;

@State(Scope.Thread)
public class AppState {
    public ConfigurableApplicationContext context;
    public RestTemplate restTemplate;

    @Setup(Level.Trial)
    public void doSetup() {
        context = SpringApplication.run(App.class);
        restTemplate = new RestTemplate();
    }

    @TearDown(Level.Trial)
    public void doTearDown() {
        context.close();
        context = null;
    }
}
