package me.loki2302.streaming;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class StreamingTest {
    @Test
    public void canWriteJson() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(stringWriter);
        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("name", "loki2302");
            jsonGenerator.writeNumberField("age", 100);
            jsonGenerator.writeEndObject();
        } finally {
            jsonGenerator.close();
        }

        String jsonString = stringWriter.toString();
        assertEquals("{\"name\":\"loki2302\",\"age\":100}", jsonString);
    }

    @Test
    public void canReadJson() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        StringReader stringReader = new StringReader("{\"name\":\"loki2302\",\"age\":100}");
        JsonParser jsonParser = jsonFactory.createParser(stringReader);
        try {
            JsonToken token = jsonParser.nextToken();
            assertEquals(JsonToken.START_OBJECT, token);

            token = jsonParser.nextToken();
            assertEquals(JsonToken.FIELD_NAME, token);
            assertEquals("name", jsonParser.getCurrentName());

            token = jsonParser.nextToken();
            assertEquals(JsonToken.VALUE_STRING, token);
            assertEquals("loki2302", jsonParser.getValueAsString());

            token = jsonParser.nextToken();
            assertEquals(JsonToken.FIELD_NAME, token);
            assertEquals("age", jsonParser.getCurrentName());

            token = jsonParser.nextToken();
            assertEquals(JsonToken.VALUE_NUMBER_INT, token);
            assertEquals(100, jsonParser.getValueAsInt());

            token = jsonParser.nextToken();
            assertEquals(JsonToken.END_OBJECT, token);
        } finally {
            jsonParser.close();
        }
    }
}
