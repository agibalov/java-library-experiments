package io.agibalov;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.*;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class CustomSimpleBeanPropertyFilterTest {
    @Test
    public void dummy() {
        User user = User.builder()
                .id("123")
                .firstName("John")
                .lastName("Smith")
                .username("jsmith")
                .posts(Arrays.asList(
                        Post.builder()
                                .id("p1")
                                .text("Post one")
                                .build(),
                        Post.builder()
                                .id("p2")
                                .text("Post two")
                                .build()))
                .build();

        assertEquals("{\"id\":\"123\"}", test(user, "id"));
        assertEquals("{\"id\":\"123\",\"firstName\":\"John\"}", test(user, "id,firstName"));
        assertEquals("{\"id\":\"123\",\"posts\":[{},{}]}", test(user, "id,posts"));
        assertEquals("{\"id\":\"123\",\"posts\":[{\"id\":\"p1\"},{\"id\":\"p2\"}]}",
                test(user, "id,posts,posts.id"));
    }

    @SneakyThrows
    private static <T> String test(T object, String fields) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(Object.class, PropertyPathPropertyFilterMixin.class);

        SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
        simpleFilterProvider.addFilter(PROPERTY_PATH_PROPERTY_FILTER_ID, new PropertyPathPropertyFilter(
                Arrays.asList(fields.split(","))));

        return objectMapper.writer(simpleFilterProvider).writeValueAsString(object);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class User {
        private String id;
        private String firstName;
        private String lastName;
        private String username;
        private List<Post> posts;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Post {
        private String id;
        private String text;
    }

    public final static String PROPERTY_PATH_PROPERTY_FILTER_ID = "propertyPathPropertyFilter";

    @JsonFilter(PROPERTY_PATH_PROPERTY_FILTER_ID)
    public static class PropertyPathPropertyFilterMixin {
    }

    public static class PropertyPathPropertyFilter extends SimpleBeanPropertyFilter {
        private final Stack<String> currentPathStack = new Stack<>();
        private final Set<String> includePaths;

        public PropertyPathPropertyFilter(Collection<String> includePaths) {
            this.includePaths = new HashSet<>(includePaths);
        }

        @Override
        public void serializeAsField(
                Object pojo,
                JsonGenerator jgen,
                SerializerProvider provider,
                PropertyWriter writer) throws Exception {

            currentPathStack.push(writer.getName());

            String pathString = currentPathStack.stream()
                    .collect(Collectors.joining("."));
            boolean shouldInclude = includePaths.stream()
                    .anyMatch(pathToInclude -> pathToInclude.startsWith(pathString));
            if (shouldInclude) {
                writer.serializeAsField(pojo, jgen, provider);
            } else {
                writer.serializeAsOmittedField(pojo, jgen, provider);
            }

            currentPathStack.pop();
        }
    }
}
