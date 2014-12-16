package me.loki2302.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class DateSerializationTest {
    private final static DateTime TEST_DATETIME =
            new DateTime(DateTimeZone.forTimeZone(TimeZone.getTimeZone("UTC")))
                    .withDate(2014, 12, 16)
                    .withTime(21, 36, 13, 0);

    @Test
    public void canSerializeAndDeserializeDateAsLongMilliseconds() throws IOException {
        Post post = new Post();
        post.createdAt = TEST_DATETIME.toDate();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(post);

        PostWithLongDate postWithStringDate = objectMapper.readValue(json, PostWithLongDate.class);
        assertEquals(TEST_DATETIME.getMillis(), postWithStringDate.createdAt);

        post = objectMapper.readValue(json, Post.class);
        assertEquals(TEST_DATETIME, new DateTime(
                post.createdAt,
                DateTimeZone.forTimeZone(TimeZone.getTimeZone("UTC"))));
    }

    public static class Post {
        public Date createdAt;
    }

    public static class PostWithLongDate {
        public long createdAt;
    }
}
