package io.agibalov;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.time.Instant;

@Controller("/hello")
public class HelloController {
    @Get(produces = MediaType.TEXT_PLAIN)
    public String index() {
        return String.format("Hello world %s", Instant.now());
    }
}
