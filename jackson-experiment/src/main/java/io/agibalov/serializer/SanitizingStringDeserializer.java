package io.agibalov.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;

public class SanitizingStringDeserializer extends JsonDeserializer<String> implements ContextualDeserializer {
    @Override
    public JsonDeserializer<?> createContextual(
            DeserializationContext ctxt,
            BeanProperty property) throws JsonMappingException {

        Trim trimAnnotation = property.getAnnotation(Trim.class);
        boolean shouldTrim = trimAnnotation != null;

        return new SanitizationAwareStringDeserializer(shouldTrim);
    }

    @Override
    public String deserialize(
            JsonParser jp,
            DeserializationContext ctxt) throws IOException, JsonProcessingException {

        throw new RuntimeException();
    }

    private static class SanitizationAwareStringDeserializer extends JsonDeserializer<String> {
        private final boolean shouldTrim;

        public SanitizationAwareStringDeserializer(boolean shouldTrim) {
            this.shouldTrim = shouldTrim;
        }

        @Override
        public String deserialize(
                JsonParser jp,
                DeserializationContext ctxt) throws IOException, JsonProcessingException {

            String value = jp.getValueAsString();
            if(value != null && shouldTrim) {
                value = value.trim();
            }

            return value;
        }
    }
}
