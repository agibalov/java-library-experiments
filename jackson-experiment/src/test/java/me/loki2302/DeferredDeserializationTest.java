package me.loki2302;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class DeferredDeserializationTest {
    private ObjectMapper objectMapper;

    @Before
    public void makeObjectMapper() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void canDeserializeToJsonNodeAndThenMapToType() throws IOException {
        String json = "{\"something\": \"omg\", \"extra\": {\"message\": \"hi there\" } }";
        Dto dto = objectMapper.readValue(json, Dto.class);
        assertEquals("omg", dto.something);

        Extra e = objectMapper.treeToValue(dto.extra, Extra.class);
        assertEquals("hi there", e.message);
    }

    public static class Dto {
        public String something;
        public JsonNode extra;
    }

    public static class Extra {
        public String message;
    }
}
