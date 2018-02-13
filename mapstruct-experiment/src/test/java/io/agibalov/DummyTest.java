package io.agibalov;

import lombok.Data;
import org.junit.Test;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import static org.junit.Assert.assertEquals;

public class DummyTest {
    @Test
    public void dummy() {
        PostMapper postMapper = Mappers.getMapper(PostMapper.class);

        PostEntity postEntity = new PostEntity();
        postEntity.id = "aaa";
        postEntity.title = "bbb";
        postEntity.text = "ccc";
        PostDto postDto = postMapper.postEntityToPostDto(postEntity);

        assertEquals(postEntity.id, postDto.id);
        assertEquals(postEntity.title, postDto.title);
        assertEquals(postEntity.text, postDto.text);
    }

    @Mapper
    public interface PostMapper {
        @Mapping(source = "id", target = "id")
        PostDto postEntityToPostDto(PostEntity postEntity);
    }

    @Data
    public static class PostEntity {
        private String id;
        private String title;
        private String text;
    }

    @Data
    public static class PostDto {
        private String id;
        private String title;
        private String text;
    }
}
