package me.loki2302;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * When designing the REST APIs, people often find it difficult to tell the difference between
 *
 * 1. not providing a value ({})
 * 2. providing a null value ({something:null})
 * 3. providing an empty value ({something:""})
 *
 * @JsonSetter(nulls = Nulls.AS_EMPTY) + default value in DTO appears to be a good way to address this
 * the way that undefineds and nulls automatically become meaningful values.
 */
public class DeserializeNullsAsEmptyValuesTest {
    private ObjectMapper objectMapper;

    @Before
    public void makeObjectMapper() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void whenJsonDoesNotHaveTheValueTheDefaultValuesIsKept() throws IOException {
        Person person = objectMapper.readValue("{}", Person.class);
        assertEquals("default name", person.getName());
        assertEquals(Arrays.asList("default interest"), person.getInterests());
    }

    @Test
    public void whenJsonSaysNullThePropertyIsSetToEmpty() throws IOException {
        Person person = objectMapper.readValue("{\"name\":null,\"interests\":null}", Person.class);
        assertEquals("", person.getName());
        assertEquals(Collections.emptyList(), person.getInterests());
    }

    @Data
    public static class Person {
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        private String name = "default name";

        // not specified > ["default interest"]
        // null -> []
        // [] -> []
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        private List<String> interests = Arrays.asList("default interest");
    }
}
