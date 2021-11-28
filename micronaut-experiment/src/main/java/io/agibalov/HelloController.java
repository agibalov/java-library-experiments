package io.agibalov;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

@Controller
public class HelloController {
    @Inject
    TimeService timeService;

    @Property(name = "app.hello-message-suffix")
    String helloMessageSuffix;

    @Get(produces = MediaType.TEXT_PLAIN)
    public String index() {
        return String.format("this is the index page. suffix: %s", helloMessageSuffix);
    }

    @Get(uri = "hello", produces = MediaType.TEXT_PLAIN)
    public String hello() {
        return String.format("Hello world %s", timeService.getTimeString());
    }
}
