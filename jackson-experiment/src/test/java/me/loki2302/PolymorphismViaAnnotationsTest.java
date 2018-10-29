package me.loki2302;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PolymorphismViaAnnotationsTest {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void canSerializeAndDeserialize() throws IOException {
        Bug bug = new Bug();
        bug.id = "123";
        bug.bugDescription = "does not make me happy";
        String json = OBJECT_MAPPER.writeValueAsString(bug);
        Issue issue = OBJECT_MAPPER.readValue(json, Issue.class);
        assertTrue(issue instanceof Bug);
        Bug bug2 = (Bug)issue;
        assertEquals("123", bug2.id);
        assertEquals("does not make me happy", bug2.bugDescription);
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Bug.class, name = "bug"),
            @JsonSubTypes.Type(value = Task.class, name = "task")
    })
    public static abstract class Issue {
        public String id;
    }

    public static class Bug extends Issue {
        public String bugDescription;
    }

    public static class Task extends Issue {
        public String taskDescription;
    }
}
