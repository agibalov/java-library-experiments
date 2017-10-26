package me.loki2302;

import org.junit.BeforeClass;
import org.junit.Test;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReduceTest {
    private static ModelMapper modelMapper;
    private static BlogEntity blogEntity;

    @BeforeClass
    public static void setUpModelMapper() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldMatchingEnabled(true);
        modelMapper.addConverter(new Converter<List, Integer>() {
            @Override
            public Integer convert(MappingContext<List, Integer> context) {
                return context.getSource().size();
            }
        });
    }

    @BeforeClass
    public static void buildBlogEntity() {
        PostEntity postEntity1 = new PostEntity();
        postEntity1.id = 111L;
        postEntity1.text = "hello";

        PostEntity postEntity2 = new PostEntity();
        postEntity2.id = 222L;
        postEntity2.text = "there";

        blogEntity = new BlogEntity();
        blogEntity.id = 123L;
        blogEntity.name = "loki2302";
        blogEntity.posts = Arrays.asList(postEntity1, postEntity2);
    }

    @Test
    public void canMapCollectionAsItemCount() {
        BriefBlogDTO briefBlogDTO = modelMapper.map(blogEntity, BriefBlogDTO.class);
        assertEquals(2, (int)briefBlogDTO.posts);
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

    public static class BriefBlogDTO {
        public long id;
        public String name;
        public Integer posts; // can't make Converter<xxx, int>
    }
}
