package io.agibalov;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpUrlTest {
    @Test
    public void canBuildUrlFromScratch() {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("example.org")
                .addPathSegment("path1")
                .addPathSegment("path2")
                .addQueryParameter("query1", "hello world")
                .addQueryParameter("query2", "hi there")
                .fragment("fragment1")
                .build();
        assertEquals("http://example.org/path1/path2?query1=hello%20world&query2=hi%20there#fragment1",
                httpUrl.toString());
    }

    @Test
    public void canParseUrl() {
        HttpUrl httpUrl = HttpUrl.get("http://example.org/path1/path2?query1=hello%20world&query2=hi%20there#fragment1");
        assertEquals("http", httpUrl.scheme());
        assertEquals("example.org", httpUrl.host());
        assertEquals(Arrays.asList("path1", "path2"), httpUrl.pathSegments());
        assertEquals(2, httpUrl.querySize());
        assertEquals("query1", httpUrl.queryParameterName(0));
        assertEquals("hello world", httpUrl.queryParameterValue(0));
        assertEquals("query2", httpUrl.queryParameterName(1));
        assertEquals("hi there", httpUrl.queryParameterValue(1));
        assertEquals("fragment1", httpUrl.fragment());
    }
}
