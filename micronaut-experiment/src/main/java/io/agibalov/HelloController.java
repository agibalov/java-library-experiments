package io.agibalov;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HelloController {
    @Inject
    TimeService timeService;

    @Inject
    HelloMessageService helloMessageService;

    @Get(produces = MediaType.TEXT_PLAIN)
    public String index() {
        log.info("index() called!");
        return helloMessageService.getMessage();
    }

    @Get(uri = "hello", produces = MediaType.TEXT_PLAIN)
    public String hello() {
        return String.format("Hello world %s", timeService.getTimeString());
    }
}
