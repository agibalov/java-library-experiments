package me.loki2302;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Consumes("application/json")
@Produces("application/json")
@Path("/calculator")
public interface CalculatorApi {
    @GET
    @Path("/addNumbers")
    int addNumbers(
            @QueryParam("a") int a, 
            @QueryParam("b") int b);
}