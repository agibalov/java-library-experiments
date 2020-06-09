package io.agibalov;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.*;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class CsvJsonAnySetterTest {
    @Test
    public void canReadCsv() throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();
        MappingIterator<User> userMappingIterator = csvMapper.readerFor(User.class)
                .with(csvSchema)
                .readValues("id,name,meta1,meta2\n123,jsmith,xxx,yyy\n234,qwerty,abc,def\n");

        List<User> users = userMappingIterator.readAll();
        assertEquals(Arrays.asList(
                User.builder()
                        .id("123")
                        .name("jsmith")
                        .extras(new HashMap<String, Object>() {{
                            put("meta1", "xxx");
                            put("meta2", "yyy");
                        }})
                        .build(),
                User.builder()
                        .id("234")
                        .name("qwerty")
                        .extras(new HashMap<String, Object>() {{
                            put("meta1", "abc");
                            put("meta2", "def");
                        }})
                        .build()
        ), users);
        System.out.printf("%s\n", users);
    }

    @Test
    public void canWriteCsv() throws IOException {
        List<User> usersToWrite = Arrays.asList(
                User.builder()
                        .id("123")
                        .name("jsmith")
                        .extras(new HashMap<String, Object>() {{
                            put("meta1", "xxx");
                            put("meta2", "yyy");
                        }})
                        .build(),
                User.builder()
                        .id("234")
                        .name("qwerty")
                        .extras(new HashMap<String, Object>() {{
                            put("meta1", "abc");
                            put("meta2", "def");
                        }})
                        .build()
        );

        List<String> extraColumnNames = usersToWrite.stream()
                .flatMap(u -> u.getExtras().keySet().stream())
                .distinct()
                .collect(Collectors.toList());

        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = CsvSchema.builder()
                .addColumnsFrom(csvMapper.schemaFor(User.class))
                .addColumns(extraColumnNames, CsvSchema.ColumnType.STRING)
                .build();
        ObjectWriter objectWriter = csvMapper.writer(csvSchema).forType(User.class);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SequenceWriter sequenceWriter = objectWriter.writeValues(baos);
        sequenceWriter.writeAll(usersToWrite);
        sequenceWriter.flush();

        assertEquals("123,jsmith,yyy,xxx\n234,qwerty,def,abc\n", baos.toString());
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonPropertyOrder({"id", "name"})
    public static class User {
        private String id;
        private String name;

        @JsonIgnore
        // Lombok warning: @Builder will ignore the initializing expression entirely. If you want the
        // initializing expression to serve as default, add @Builder.Default. If it is not supposed to
        // be settable during building, make the field final.
        // 1. Making it final means the builder won't have a .extras() part
        // 2. Annotating with @Build.Default makes it null when User is constructed using "new"
        private Map<String, Object> extras = new HashMap<>();

        @JsonAnySetter
        public void putExtra(String key, Object value) {
            extras.put(key, value);
        }

        @JsonAnyGetter
        public Map<String, Object> getExtras() {
            return extras;
        }
    }
}
