package io.agibalov.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

import java.io.IOException;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class JodaDateTimeSerializationTest {
    private final static DateTime TEST_DATETIME =
            new DateTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone("UTC")))
                    .withDate(2014, 12, 16)
                    .withTime(21, 36, 13, 0);

    @Test
    public void canSerializeAndDeserializeDateTimeAsLongMilliseconds() throws IOException {
        Post post = new Post();
        post.createdAt = TEST_DATETIME;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
        String json = objectMapper.writeValueAsString(post);

        PostWithLongDate postWithLongDate = objectMapper.readValue(json, PostWithLongDate.class);
        assertEquals(TEST_DATETIME.getMillis(), postWithLongDate.createdAt);

        post = objectMapper.readValue(json, Post.class);
        assertEquals(TEST_DATETIME, new DateTime(
                post.createdAt,
                DateTimeZone.forTimeZone(TimeZone.getTimeZone("UTC"))));
    }

    @Test
    public void canSerializeAndDeserializeDateTimeAsISO8601String() throws IOException {
        Post post = new Post();
        post.createdAt = TEST_DATETIME;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String json = objectMapper.writeValueAsString(post);

        PostWithStringDate postWithStringDate = objectMapper.readValue(json, PostWithStringDate.class);
        assertEquals(TEST_DATETIME.toString(ISODateTimeFormat.dateTime()), postWithStringDate.createdAt);

        post = objectMapper.readValue(json, Post.class);
        assertEquals(TEST_DATETIME, post.createdAt);
    }

    public static class Post {
        public DateTime createdAt;
    }

    public static class PostWithLongDate {
        public long createdAt;
    }

    public static class PostWithStringDate {
        public String createdAt;
    }
}
