package io.agibalov;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

@Controller
public class HelloController {
    @Inject
    TimeService timeService;

    @Get(produces = MediaType.TEXT_PLAIN)
    public String index() {
        return "this is the index page";
    }

    @Get(uri = "hello", produces = MediaType.TEXT_PLAIN)
    public String hello() {
        return String.format("Hello world %s", timeService.getTimeString());
    }
}
