package me.loki2302.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SanitizingStringDeserializerTest {
    private ObjectMapper objectMapper;

    @Before
    public void setUpObjectMapper() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new SanitizingStringDeserializer());
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
    }

    @After
    public void cleanUpObjectMapper() {
        objectMapper = null;
    }

    @Test
    public void dummy() throws IOException {
        SignInRequestDto originalDto = new SignInRequestDto();
        originalDto.email = "   hello   ";
        originalDto.password = " qwerty ";

        String originalDtoJson = objectMapper.writeValueAsString(originalDto);
        SignInRequestDto restoredDto = objectMapper.readValue(originalDtoJson, SignInRequestDto.class);

        assertEquals("hello", restoredDto.email);
        assertEquals(" qwerty ", restoredDto.password);
    }

    public static class SignInRequestDto {
        @Trim
        public String email;
        public String password;
    }

}
