package me.loki2302;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

@Consumes("application/json")
@Produces("application/json")
@Path("/person")
public class PersonResource {
    private List<Person> people = new ArrayList<Person>();
    
    public PersonResource() {
        people.add(makePerson(1, "loki2302"));
        people.add(makePerson(2, "millasa"));
    }
    
    @GET
    @Path("/")
    public List<Person> getAll() {
        return people;
    }
    
    @GET
    @Path("/{id}")
    public Person getOne(@PathParam("id") int id) {
        for(Person person : people) {
            if(person.id == id) {
                return person;
            }
        }
        
        throw new PersonNotFoundException(id);
    }
    
    @GET
    @Path("/{id}/image")
    public Response getImage(@PathParam("id") int id) {
        Person person = getOne(id);
        
        File imageFile = QRCode
            .from(person.name)
            .withSize(200, 200)
            .to(ImageType.PNG)
            .file();
        
        return Response.ok(imageFile, MediaType.APPLICATION_OCTET_STREAM_TYPE).build();        
    }
    
    private static Person makePerson(int id, String name) {
        Person person = new Person();
        person.id = id;
        person.name = name;
        person.imageUrl = makeImageUrl(id);
        return person;
    }
    
    // TODO: understand how to generate links to related resources properly
    private static String makeImageUrl(int id) {
        return String.format("/api/person/%d/image", id);
    }    
}