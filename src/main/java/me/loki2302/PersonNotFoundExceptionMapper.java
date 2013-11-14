package me.loki2302;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class PersonNotFoundExceptionMapper implements ExceptionMapper<PersonNotFoundException> {
    @Override
    public Response toResponse(PersonNotFoundException exception) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.message = String.format("There's no person with id %d", exception.getId());
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(errorDetails)
                .build();
    }        
}