package io.agibalov;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DummyTest {
    @Test
    public void canExtractSubObject() {
        Map<String, Object> bicycle = readContext.read("$.store.bicycle");
        assertEquals("red", bicycle.get("color"));
        assertEquals(19.95, (Double)bicycle.get("price"), 0.0001);
    }

    @Test
    public void canExtractSingleValue() {
        assertEquals("red", readContext.read("$.store.bicycle.color", String.class));
    }

    @Test
    public void canExtractByIndex() {
        assertEquals("Evelyn Waugh", readContext.read("$.store.book[1].author", String.class));
    }

    @Test
    public void canExtractByCondition() {
        // can't make it return a single item instead of a list:
        // https://github.com/json-path/JsonPath/issues/272
        assertEquals(Arrays.asList("Moby Dick"),
                readContext.read("$.store.book[?(@.author == 'Herman Melville')].title", List.class));
    }

    @Test
    public void canGetEmptyCollectionWhenNothingMatchesTheCondition() {
        assertEquals(Collections.emptyList(),
                readContext.read("$.store.book[?(@.author == 'Robert Asprin')].title", List.class));
    }

    @Test
    public void canCount() {
        assertEquals(4, (int)readContext.read("$.store.book.length()", Integer.class));
    }

    private final ReadContext readContext = JsonPath.parse("{\n" +
            "    \"store\": {\n" +
            "        \"book\": [\n" +
            "            {\n" +
            "                \"category\": \"reference\",\n" +
            "                \"author\": \"Nigel Rees\",\n" +
            "                \"title\": \"Sayings of the Century\",\n" +
            "                \"price\": 8.95\n" +
            "            },\n" +
            "            {\n" +
            "                \"category\": \"fiction\",\n" +
            "                \"author\": \"Evelyn Waugh\",\n" +
            "                \"title\": \"Sword of Honour\",\n" +
            "                \"price\": 12.99\n" +
            "            },\n" +
            "            {\n" +
            "                \"category\": \"fiction\",\n" +
            "                \"author\": \"Herman Melville\",\n" +
            "                \"title\": \"Moby Dick\",\n" +
            "                \"isbn\": \"0-553-21311-3\",\n" +
            "                \"price\": 8.99\n" +
            "            },\n" +
            "            {\n" +
            "                \"category\": \"fiction\",\n" +
            "                \"author\": \"J. R. R. Tolkien\",\n" +
            "                \"title\": \"The Lord of the Rings\",\n" +
            "                \"isbn\": \"0-395-19395-8\",\n" +
            "                \"price\": 22.99\n" +
            "            }\n" +
            "        ],\n" +
            "        \"bicycle\": {\n" +
            "            \"color\": \"red\",\n" +
            "            \"price\": 19.95\n" +
            "        }\n" +
            "    },\n" +
            "    \"expensive\": 10\n" +
            "}");
}
