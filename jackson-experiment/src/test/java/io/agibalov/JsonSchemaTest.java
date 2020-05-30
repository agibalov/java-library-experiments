package io.agibalov;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class JsonSchemaTest {
    @Test
    public void canGenerateSchema() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator(objectMapper);
        JsonNode jsonSchema = jsonSchemaGenerator.generateJsonSchema(MyDto.class);
        assertEquals("object", jsonSchema.at("/type").asText());
        assertEquals("string", jsonSchema.at("/properties/id/type").asText());
        assertEquals("string", jsonSchema.at("/properties/name/type").asText());
        assertEquals("integer", jsonSchema.at("/properties/age/type").asText());
        assertEquals("id", jsonSchema.at("/required/0").asText());
        assertEquals("name", jsonSchema.at("/required/1").asText());
        assertEquals("age", jsonSchema.at("/required/2").asText());
    }

    @Test
    public void canValidateAgainstSchema() throws IOException, ProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator(objectMapper);
        JsonNode jsonSchema = jsonSchemaGenerator.generateJsonSchema(MyDto.class);

        JsonValidator jsonValidator = JsonSchemaFactory.byDefault().getValidator();
        ProcessingReport processingReport = jsonValidator.validate(
                jsonSchema,
                objectMapper.readTree("{\"id\":\"123\"}"));

        List<ProcessingMessage> processingMessages = StreamSupport.stream(processingReport.spliterator(), false)
                .collect(Collectors.toList());

        assertEquals(1, processingMessages.size());

        ProcessingMessage processingMessage = processingMessages.get(0);
        assertEquals("object has missing required properties ([\"age\",\"name\"])",
                processingMessage.getMessage());
    }

    public static class MyDto {
        @JsonProperty(required = true)
        public String id;

        @JsonProperty(required = true)
        public String name;

        public int age;
    }
}
