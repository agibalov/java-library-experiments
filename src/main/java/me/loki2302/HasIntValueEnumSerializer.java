package me.loki2302;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class HasIntValueEnumSerializer extends JsonSerializer<Enum> {
    @Override
    public void serialize(
            Enum value,
            JsonGenerator jgen,
            SerializerProvider provider) throws IOException, JsonProcessingException {

        if(!(value instanceof HasIntValue)) {
            throw new RuntimeException("All enums are supposed to implement HasIntValue");
        }

        HasIntValue hasIntValue = (HasIntValue)value;
        int intValue = hasIntValue.getIntValue();
        jgen.writeNumber(intValue);
    }

    @Override
    public Class<Enum> handledType() {
        return Enum.class;
    }
}
