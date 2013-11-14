package me.loki2302;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


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
        
        // TODO: how do I throw 404?
        
        return null;
    }
    
    private static Person makePerson(int id, String name) {
        Person person = new Person();
        person.id = id;
        person.name = name;
        return person;
    }
}