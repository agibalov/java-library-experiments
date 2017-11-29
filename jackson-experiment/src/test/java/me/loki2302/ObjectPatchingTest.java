package me.loki2302;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ObjectPatchingTest {
    @Test
    public void canMapHierarchies() throws IOException {
        Todo todo = new Todo();
        todo.title = "title";
        todo.text = "text";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.readerForUpdating(todo).readValue("{\"title\":\"new title!\"}");

        assertEquals("new title!", todo.title);
        assertEquals("text", todo.text);
    }

    public static class Todo {
        public String title;
        public String text;
    }
}
