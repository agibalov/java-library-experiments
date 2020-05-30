package io.agibalov.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Java8DateTimeSerializationTest {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    public void canDeserializeSecondsToInstant() throws IOException {
        SomeEvent someEvent = OBJECT_MAPPER.readValue(
                "{\"timestamp\": 0}",
                SomeEvent.class);
        assertEquals("1970-01-01T00:00:00Z", someEvent.timestamp.toString());
    }

    @Test
    public void canDeserializeIso8601ZToInstant() throws IOException {
        SomeEvent someEvent = OBJECT_MAPPER.readValue(
                "{\"timestamp\": \"1970-01-01T00:00:00Z\"}",
                SomeEvent.class);
        assertEquals(0, someEvent.timestamp.getEpochSecond());
    }

    @Test
    public void canDeserializeDayOfWeekAndDuration() throws IOException {
        SomeConfig someConfig = OBJECT_MAPPER.readValue(
                "{\"daysOfWeek\":[\"MONDAY\", \"WEDNESDAY\"], \"duration\": \"PT5M\"}",
                SomeConfig.class);
        assertEquals(2, someConfig.daysOfWeek.size());
        assertTrue(someConfig.daysOfWeek.contains(DayOfWeek.MONDAY));
        assertTrue(someConfig.daysOfWeek.contains(DayOfWeek.WEDNESDAY));
        assertEquals(Duration.ofMinutes(5), someConfig.duration);
    }

    public static class SomeEvent {
        public Instant timestamp;
    }

    public static class SomeConfig {
        public Set<DayOfWeek> daysOfWeek;
        public Duration duration;
    }
}
