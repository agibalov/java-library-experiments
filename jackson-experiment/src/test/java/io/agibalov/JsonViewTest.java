package io.agibalov;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class JsonViewTest {
    private static Person person;

    @BeforeClass
    public static void buildPerson() {
        person = new Person();
        person.name = "loki2302";
        person.password = "qwerty";
        person.realName = "Andrey";
        person.age = 40;
    }

    @Test
    public void canSerializeAsPublic() throws IOException {
        JsonNode node = writeWithViewAndReadAsJsonNode(Person.Public.class);

        assertEquals(1, node.size());
        assertTrue(node.has("name"));

        assertEquals("loki2302", node.get("name").asText());
    }

    @Test
    public void canSerializeAsAdmin() throws IOException {
        JsonNode node = writeWithViewAndReadAsJsonNode(Person.Admin.class);

        assertEquals(2, node.size());
        assertTrue(node.has("name"));
        assertTrue(node.has("password"));

        assertEquals("loki2302", node.get("name").asText());
        assertEquals("qwerty", node.get("password").asText());
    }

    @Test
    public void canSerializeAsIntamit() throws IOException {
        JsonNode node = writeWithViewAndReadAsJsonNode(Person.Intamit.class);

        assertEquals(4, node.size());
        assertTrue(node.has("name"));
        assertTrue(node.has("password"));
        assertTrue(node.has("realName"));
        assertTrue(node.has("age"));

        assertEquals("loki2302", node.get("name").asText());
        assertEquals("qwerty", node.get("password").asText());
        assertEquals("Andrey", node.get("realName").asText());
        assertEquals(40, node.get("age").asInt());
    }

    private JsonNode writeWithViewAndReadAsJsonNode(Class<?> viewClass) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper
                .writerWithView(viewClass)
                .writeValueAsString(person);
        JsonNode node = objectMapper.readTree(json);

        return node;
    }

    private static class Person {
        public static class Public {}
        public static class Admin extends Public {}
        public static class Intamit extends Admin {}

        @JsonView(Public.class) public String name;
        @JsonView(Admin.class) public String password;
        @JsonView(Intamit.class) public String realName;
        @JsonView(Intamit.class) public int age;
    }
}
