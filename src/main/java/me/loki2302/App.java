package me.loki2302;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        PostEntity postEntity1 = new PostEntity();
        postEntity1.id = 111L;
        postEntity1.text = "hello";

        PostEntity postEntity2 = new PostEntity();
        postEntity2.id = 222L;
        postEntity2.text = "there";

        BlogEntity blogEntity = new BlogEntity();
        blogEntity.id = 123L;
        blogEntity.name = "loki2302";
        blogEntity.posts = Arrays.asList(postEntity1, postEntity2);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldMatchingEnabled(true);
        modelMapper.addConverter(new Converter<List, Integer>() {
            @Override
            public Integer convert(MappingContext<List, Integer> context) {
                return context.getSource().size();
            }
        });

        BriefBlogDTO briefBlogDTO = modelMapper.map(blogEntity, BriefBlogDTO.class);
        System.out.println(briefBlogDTO);
    }

    public static class BlogEntity {
        public Long id;
        public String name;
        public List<PostEntity> posts;
    }

    public static class PostEntity {
        public Long id;
        public String text;
    }

    public static abstract class FancyToString {
        @Override
        public String toString() {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
            try {
                return objectMapper.writeValueAsString(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class BriefBlogDTO extends FancyToString {
        public long id;
        public String name;
        public Integer posts; // can't make Converter<xxx, int>
    }
}
