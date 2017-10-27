package me.loki2302;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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

public class CsvTest {
    @Test
    public void canWriteCsv() throws JsonProcessingException {
        List<Person> persons = Arrays.asList(
                new Person(1, "john smith", null),
                new Person(2, "andrey", "idiot")
        );

        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = csvMapper.schemaFor(Person.class).rebuild()
                .setNullValue("THENULL")
                .setQuoteChar('`')
                .setColumnSeparator('|')
                .setUseHeader(true)
                .build();
        String csv = csvMapper.writer(csvSchema).writeValueAsString(persons);

        assertEquals("`id`|`name`|`comments`\n" +
                "1|`john smith`|THENULL\n" +
                "2|`andrey`|`idiot`\n", csv);
    }

    @Test
    public void canReadCsv() throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = csvMapper.schemaFor(Person.class);
        MappingIterator<Person> personMappingIterator = csvMapper.readerFor(Person.class)
                .with(csvSchema)
                .readValues("123,andrey,\"idiot\"\n");

        List<Person> persons = personMappingIterator.readAll();
        assertEquals(1, persons.size());
        assertEquals(123, persons.get(0).getId());
        assertEquals("andrey", persons.get(0).getName());
        assertEquals("idiot", persons.get(0).getComments());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonPropertyOrder({"id", "name", "comments"})
    public static class Person {
        private int id;
        private String name;
        private String comments;
    }
}
