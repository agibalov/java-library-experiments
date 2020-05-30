package io.agibalov;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CsvTest {
    @Test
    public void canWriteCsv() throws JsonProcessingException {
        List<Person> persons = Arrays.asList(
                new Person(1, "john smith", null, new PersonDetails(100, "yes")),
                new Person(2, "andrey", "idiot", null)
        );

        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = csvMapper.schemaFor(Person.class).rebuild()
                .setNullValue("THENULL")
                .setQuoteChar('`')
                .setColumnSeparator('|')
                .setUseHeader(true)
                .build();
        String csv = csvMapper.writer(csvSchema).writeValueAsString(persons);

        assertEquals("`id`|`name`|`comments`|`sex`|`age`\n" +
                "1|`john smith`|THENULL|`yes`|100\n" +
                "2|`andrey`|`idiot`||\n", csv);
    }

    // This test uses ReadablePerson instead of Person because Jackson doesn't support
    // reading @JsonUnwrapped attributes from CSV.
    @Test
    public void canReadCsv() throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = csvMapper.schemaFor(ReadablePerson.class);
        MappingIterator<ReadablePerson> personMappingIterator = csvMapper.readerFor(ReadablePerson.class)
                .with(csvSchema)
                .readValues("123,andrey,\"idiot\"\n");

        List<ReadablePerson> persons = personMappingIterator.readAll();
        assertEquals(1, persons.size());
        assertEquals(123, persons.get(0).getId());
        assertEquals("andrey", persons.get(0).getName());
        assertEquals("idiot", persons.get(0).getComments());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonPropertyOrder({"id", "name", "comments", "details"})
    public static class Person {
        private int id;
        private String name;
        private String comments;

        @JsonUnwrapped
        private PersonDetails details;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonPropertyOrder({"sex", "age"})
    public static class PersonDetails {
        private int age;
        private String sex;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonPropertyOrder({"id", "name", "comments"})
    public static class ReadablePerson {
        private int id;
        private String name;
        private String comments;
    }
}
