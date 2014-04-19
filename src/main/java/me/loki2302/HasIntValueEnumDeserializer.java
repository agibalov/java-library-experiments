package me.loki2302;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;

public class HasIntValueEnumDeserializer extends JsonDeserializer<Enum> implements ContextualDeserializer {
    @Override
    public JsonDeserializer<?> createContextual(
            DeserializationContext ctxt,
            BeanProperty property) throws JsonMappingException {

        Class<? extends Enum> propertyClass = (Class<? extends Enum>)property.getType().getRawClass();
        if(!HasIntValue.class.isAssignableFrom(propertyClass)) {
            throw new RuntimeException("All enums are supposed to implement HasIntValue");
        }

        return new TargetEnumClassAwareEnumDeserializer(propertyClass);
    }

    @Override
    public Enum deserialize(
            JsonParser jp,
            DeserializationContext ctxt) throws IOException, JsonProcessingException {

        throw new RuntimeException();
    }

    public static class TargetEnumClassAwareEnumDeserializer extends JsonDeserializer<Enum> {
        private final Class<? extends Enum> enumClass;

        public TargetEnumClassAwareEnumDeserializer(Class<? extends Enum> enumClass) {
            this.enumClass = enumClass;
        }

        @Override
        public Enum deserialize(
                JsonParser jp,
                DeserializationContext ctxt) throws IOException, JsonProcessingException {

            int value = jp.readValueAs(int.class);

            Object[] enumConstants = enumClass.getEnumConstants();
            for(Object enumConstant : enumConstants) {
                HasIntValue hasIntValue = (HasIntValue)enumConstant;
                if(hasIntValue.getIntValue() != value) {
                    continue;
                }

                return Enum.valueOf(enumClass, enumConstant.toString());
            }

            throw new RuntimeException();
        }
    }
}
