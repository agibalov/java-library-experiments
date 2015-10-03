package me.loki2302;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ListSerializationTest {
    private ObjectMapper objectMapper;

    @Before
    public void makeObjectMapper() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void canSerializeANullListAsNull() throws JsonProcessingException {
        Issues issues = new Issues();
        issues.issues = null;
        String json = objectMapper.writeValueAsString(issues);
        assertEquals("{\"issues\":null}", json);
    }

    @Test
    public void canSerializeANullListAsNothing() throws JsonProcessingException {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        Issues issues = new Issues();
        issues.issues = null;
        String json = objectMapper.writeValueAsString(issues);
        assertEquals("{}", json);
    }

    @Test
    public void canDeserializeANullListAsNull() throws IOException {
        assertNull(objectMapper.readValue("{}", Issues.class).issues);
        assertNull(objectMapper.readValue("{\"issues\":null}", Issues.class).issues);
    }

    @Test
    public void canDeserializeAnEmptyList() throws IOException {
        Issues issues = objectMapper.readValue("{\"issues\": []}", Issues.class);
        assertNotNull(issues.issues);
        assertTrue(issues.issues.isEmpty());
    }

    @Test
    public void cantSerializeAPolymorphicCollectionAsIs() throws IOException {
        List<Issue> issues = Arrays.asList(
                makeBug("bug-1"),
                makeTask("task-1"));
        String json = objectMapper.writeValueAsString(issues);
        assertFalse(json.contains("type"));
    }

    @Test
    public void canSerializeAPolymorphicCollectionWhenUsingInheritedCollectionType() throws JsonProcessingException {
        List<Issue> issues = Arrays.asList(
                makeBug("bug-1"),
                makeTask("task-1"));
        String json = objectMapper.writeValueAsString(new ArrayList<Issue>(issues) {
        });
        assertTrue(json.contains("type"));
    }

    @Test
    public void canSerializeAPolymorphicCollectionWhenSupplyingATypeExplicitly() throws JsonProcessingException {
        List<Issue> issues = Arrays.asList(
                makeBug("bug-1"),
                makeTask("task-1"));
        String json = objectMapper.writerWithType(new TypeReference<List<Issue>>() {}).writeValueAsString(issues);
        assertTrue(json.contains("type"));
    }

    @Test
    public void canDeserializeAPolymorphicCollection() throws IOException {
        String json = "[{\"type\": \"bug\", \"id\": \"bug-1\"}, {\"type\": \"task\", \"id\": \"task-1\"}]";
        List<Issue> issues = objectMapper.readValue(json, new TypeReference<List<Issue>>() {});
        assertEquals(2, issues.size());
        assertTrue(issues.get(0) instanceof Bug);
        assertTrue(issues.get(1) instanceof Task);
    }

    private static Bug makeBug(String id) {
        Bug bug = new Bug();
        bug.id = id;
        return bug;
    }

    private static Task makeTask(String id) {
        Task task = new Task();
        task.id = id;
        return task;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Bug.class, name = "bug"),
            @JsonSubTypes.Type(value = Task.class, name = "task")
    })
    public static abstract class Issue {
        public String id;
    }

    public static class Bug extends Issue {
    }

    public static class Task extends Issue {
    }

    public static class Issues {
        public List<Issue> issues;
    }
}
