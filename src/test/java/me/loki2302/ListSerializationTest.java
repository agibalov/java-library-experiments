package me.loki2302;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ListSerializationTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void cantSerializeAPolymorphicCollectionAsIs() throws IOException {
        List<Issue> issues = Arrays.asList(
                makeBug("bug-1", "hello"),
                makeTask("task-1", "hi there"));
        String json = objectMapper.writeValueAsString(issues);
        assertFalse(json.contains("type"));
    }

    @Test
    public void canSerializeAPolymorphicCollectionWhenUsingInheritedCollectionType() throws JsonProcessingException {
        List<Issue> issues = Arrays.asList(
                makeBug("bug-1", "hello"),
                makeTask("task-1", "hi there"));
        String json = objectMapper.writeValueAsString(new ArrayList<Issue>(issues) {
        });
        assertTrue(json.contains("type"));
    }

    @Test
    public void canSerializeAPolymorphicCollectionWhenSupplyingATypeExplicitly() throws JsonProcessingException {
        List<Issue> issues = Arrays.asList(
                makeBug("bug-1", "hello"),
                makeTask("task-1", "hi there"));
        String json = objectMapper.writerWithType(new TypeReference<List<Issue>>() {}).writeValueAsString(issues);
        assertTrue(json.contains("type"));
    }

    private static Bug makeBug(String id, String description) {
        Bug bug = new Bug();
        bug.id = id;
        bug.description = description;
        return bug;
    }

    private static Task makeTask(String id, String description) {
        Task task = new Task();
        task.id = id;
        task.description = description;
        return task;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Bug.class, name = "bug"),
            @JsonSubTypes.Type(value = Task.class, name = "task")
    })
    public static abstract class Issue {
        public String id;
        public String description;
    }

    public static class Bug extends Issue {
    }

    public static class Task extends Issue {
    }
}
