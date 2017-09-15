package me.loki2302.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;

import java.io.IOException;
import java.time.Instant;

import static org.junit.Assert.assertEquals;

public class Java8DateTimeSerializationTest {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    public void canDeserializeSecondsToInstant() throws IOException {
        SomeEvent someEvent = OBJECT_MAPPER.readValue("{\"timestamp\": 0}", SomeEvent.class);
        assertEquals("1970-01-01T00:00:00Z", someEvent.timestamp.toString());
    }

    @Test
    public void canDeserializeIso8601ZToInstant() throws IOException {
        SomeEvent someEvent = OBJECT_MAPPER.readValue("{\"timestamp\": \"1970-01-01T00:00:00Z\"}", SomeEvent.class);
        assertEquals(0, someEvent.timestamp.getEpochSecond());
    }

    public static class SomeEvent {
        public Instant timestamp;
    }
}
