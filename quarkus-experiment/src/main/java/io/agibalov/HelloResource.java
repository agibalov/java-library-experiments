package io.agibalov;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("/")
public class HelloResource {
    @Inject
    TimeService timeService;

    @Inject
    HelloMessageService helloMessageService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String index() {
        log.info("index() called!");
        return helloMessageService.getMessage();
    }

    @GET
    @Path("hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return String.format("Hello world %s", timeService.getTimeString());
    }
}
