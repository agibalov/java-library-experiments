package me.loki2302;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SimpleBeanPropertyFilterTest {
    @Test
    public void simpleBeanPropertyFilterWorks() throws JsonProcessingException {
        SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
        simpleFilterProvider.addFilter("user", SimpleBeanPropertyFilter.filterOutAllExcept("id", "firstName", "posts"));
        simpleFilterProvider.addFilter("userPosts", SimpleBeanPropertyFilter.filterOutAllExcept("id"));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setFilterProvider(simpleFilterProvider);

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
        String s = objectMapper.writer(simpleFilterProvider).writeValueAsString(user);
        assertEquals("{\"id\":\"123\",\"firstName\":\"John\",\"posts\":[{\"id\":\"p1\"},{\"id\":\"p2\"}]}", s);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonFilter("user")
    public static class User {
        private String id;
        private String firstName;
        private String lastName;
        private String username;

        @JsonFilter("userPosts")
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
}
