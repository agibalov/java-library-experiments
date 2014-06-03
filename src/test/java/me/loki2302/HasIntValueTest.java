package me.loki2302;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class HasIntValueTest {
    private ObjectMapper objectMapper;

    @Before
    public void setUpObjectMapper() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(new HasIntValueEnumSerializer());
        module.addDeserializer(Enum.class, new HasIntValueEnumDeserializer());
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
    }

    @After
    public void cleanUpObjectMapper() {
        objectMapper = null;
    }

    @Test
    public void goodGenderGetsSerializedAndDeserialized() throws IOException {
        assertEquals(GoodGender.Female, writeAndReadGood(GoodGender.Female));
        assertEquals(GoodGender.Male, writeAndReadGood(GoodGender.Male));
        assertEquals(GoodGender.Unknown, writeAndReadGood(GoodGender.Unknown));
    }

    @Test(expected = JsonMappingException.class)
    public void badGenderSerializationThrows() throws IOException {
        writeAndReadBad(BadGender.Male);
    }

    private GoodGender writeAndReadGood(GoodGender gender) throws IOException {
        GoodPersonDto personDto = new GoodPersonDto();
        personDto.gender = gender;

        String personJson = objectMapper.writeValueAsString(personDto);
        GoodPersonDto deserializedPersonDto = objectMapper.readValue(personJson, GoodPersonDto.class);

        return deserializedPersonDto.gender;
    }

    private BadGender writeAndReadBad(BadGender gender) throws IOException {
        BadPersonDto personDto = new BadPersonDto();
        personDto.gender = gender;

        String personJson = objectMapper.writeValueAsString(personDto);
        BadPersonDto deserializedPersonDto = objectMapper.readValue(personJson, BadPersonDto.class);

        return deserializedPersonDto.gender;
    }
}
