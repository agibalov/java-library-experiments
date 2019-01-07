package me.loki2302.metrics;

import me.loi2302.app.App;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = {
        App.class,
        TestConfig.class
}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public @interface FancyTest {
}
