package me.loki2302;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Guess concrete type without explicit type descriptor:
 *
 * If there's "weight", read as {@link Weight}
 * If there's "mood", read as {@link Mood}
 * throw otherwise
 */
public class CustomDeserializerTest {
    private ObjectMapper objectMapper;

    @Before
    public void makeObjectMapper() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void canReadWeight() throws IOException {
        Update update = objectMapper.readValue("{\"weight\": 123}", Update.class);
        assertTrue(update instanceof Weight);
        Weight weight = (Weight)update;
        assertEquals(123, weight.value);
    }

    @Test
    public void canReadMood() throws IOException {
        Update update = objectMapper.readValue("{\"mood\": \"saaad\"}", Update.class);
        assertTrue(update instanceof Mood);
        Mood mood = (Mood)update;
        assertEquals("saaad", mood.value);
    }

    @Test(expected = JsonParseException.class)
    public void cantReadUnknown() throws IOException {
        objectMapper.readValue("{\"something\": 3.14}", Update.class);
    }

    public static class UpdateDeserializer extends JsonDeserializer<Update> {
        @Override
        public Update deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode jsonNode = p.getCodec().readTree(p);
            if(jsonNode.has("weight")) {
                int value = jsonNode.get("weight").asInt();
                Weight weight = new Weight();
                weight.value = value;
                return weight;
            } else if(jsonNode.has("mood")) {
                String value = jsonNode.get("mood").asText();
                Mood mood = new Mood();
                mood.value = value;
                return mood;
            }

            throw new JsonParseException("Unknown update", p.getCurrentLocation());
        }
    }

    @JsonDeserialize(using = UpdateDeserializer.class)
    public static abstract class Update {
    }

    public static class Weight extends Update {
        public int value;
    }

    public static class Mood extends Update {
        public String value;
    }
}
