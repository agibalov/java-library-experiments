package io.agibalov;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.Test;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DummyTest {
    private final PostMapper POST_MAPPER = Mappers.getMapper(PostMapper.class);

    @Test
    public void canMapPostEntityToPostDto() {
        PostEntity postEntity = PostEntity.builder()
                .id("aaa")
                .title("bbb")
                .text("ccc")
                .build();
        PostDto postDto = POST_MAPPER.postEntityToPostDto(postEntity);

        assertEquals(postEntity.getId(), postDto.getId());
        assertEquals(postEntity.getTitle(), postDto.getTitle());
        assertEquals(postEntity.getText(), postDto.getText());
    }

    @Test
    public void canMapPostEntitiesToPostDtos() {
        List<PostEntity> postEntities = Arrays.asList(
                PostEntity.builder().id("aaa1").title("bbb1").text("ccc1").build(),
                PostEntity.builder().id("aaa2").title("bbb2").text("ccc2").build(),
                PostEntity.builder().id("aaa3").title("bbb3").text("ccc3").build());

        List<PostDto> postDtos = POST_MAPPER.postEntitiesToPostEntityDtos(postEntities);
        assertEquals(3, postDtos.size());
        assertEquals("aaa1", postDtos.get(0).getId());
        assertEquals("bbb1", postDtos.get(0).getTitle());
        assertEquals("ccc1", postDtos.get(0).getText());
        assertEquals("aaa2", postDtos.get(1).getId());
        assertEquals("bbb2", postDtos.get(1).getTitle());
        assertEquals("ccc2", postDtos.get(1).getText());
        assertEquals("aaa3", postDtos.get(2).getId());
        assertEquals("bbb3", postDtos.get(2).getTitle());
        assertEquals("ccc3", postDtos.get(2).getText());
    }

    @Test
    public void canMapPostEntityToEditablePostAttributesD() {
        PostEntity postEntity = PostEntity.builder()
                .id("aaa")
                .title("bbb")
                .text("ccc")
                .build();
        EditablePostAttributesDto attributesDto = POST_MAPPER.postEntityToEditablePostEntityAttributesDto(postEntity);

        assertEquals(postEntity.getTitle(), attributesDto.getTitle());
        assertEquals(postEntity.getText(), attributesDto.getText());
    }

    @Test
    public void canUpdate() {
        PostEntity postEntity = PostEntity.builder()
                .id("aaa")
                .title("bbb")
                .text("ccc")
                .build();

        EditablePostAttributesDto attributesDto = new EditablePostAttributesDto();
        attributesDto.setTitle("new title");
        attributesDto.setText("new text");
        POST_MAPPER.updatePostEntityWithEditablePostAttributes(postEntity, attributesDto);

        assertEquals("aaa", postEntity.getId());
        assertEquals("new title", postEntity.getTitle());
        assertEquals("new text", postEntity.getText());
    }

    @Mapper
    public interface PostMapper {
        PostDto postEntityToPostDto(PostEntity postEntity);
        List<PostDto> postEntitiesToPostEntityDtos(List<PostEntity> postEntities);
        EditablePostAttributesDto postEntityToEditablePostEntityAttributesDto(PostEntity postEntity);

        // NOTE: because EditablePostAttributesDto doesn't have an 'id' attribute, there's a warning:
        // warning: Unmapped target property: "id"
        @Mapping(target = "id", ignore = true)
        void updatePostEntityWithEditablePostAttributes(
                @MappingTarget PostEntity postEntity,
                EditablePostAttributesDto attributesDto);
    }

    @Data
    @Builder
    public static class PostEntity {
        private String id;
        private String title;
        private String text;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class PostDto extends EditablePostAttributesDto {
        private String id;
    }

    @Data
    public static class EditablePostAttributesDto {
        private String title;
        private String text;
    }
}
