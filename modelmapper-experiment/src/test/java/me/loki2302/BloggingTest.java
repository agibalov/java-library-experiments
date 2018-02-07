package me.loki2302;

import lombok.*;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BloggingTest {
    private ModelMapper modelMapper;

    {
        modelMapper = new ModelMapper();
    }

    @Test
    public void canConvertPostEntityToPostDto() {
        PostEntity postEntity = postEntityBuilder("111").build();

        PostDto postDto = modelMapper.map(postEntity, PostDto.class);

        assertPostDtoRepresentsPostEntity(postEntity, postDto);
    }

    @Test
    public void canConvertPostEntityListToPostDtoList() {
        List<PostEntity> postEntities = Arrays.asList(
                postEntityBuilder("111").build(),
                postEntityBuilder("222").build());

        List<PostDto> postDtos = modelMapper.map(postEntities, new TypeToken<List<PostDto>>() {}.getType());

        assertEquals(2, postDtos.size());
        assertPostDtoRepresentsPostEntity(postEntities.get(0), postDtos.get(0));
        assertPostDtoRepresentsPostEntity(postEntities.get(1), postDtos.get(1));
    }

    @Test
    public void canConvertPostEntityToEditablePostAttributesDto() {
        PostEntity postEntity = postEntityBuilder("111").build();

        EditablePostAttributesDto editablePostAttributesDto = modelMapper.map(postEntity, EditablePostAttributesDto.class);

        assertEditablePostAttributesDtoRepresentsPostEntity(postEntity, editablePostAttributesDto);
    }

    @Test
    public void canUpdatePostEntityWithEditablePostAttributesDto() {
        PostEntity postEntity0 = postEntityBuilder("111").build();
        PostEntity postEntity = postEntityBuilder("111").build();

        EditablePostAttributesDto editablePostAttributesDto = EditablePostAttributesDto.editablePostAttributesBuilder()
                .title("NEW TITLE")
                .text("NEW TEXT")
                .build();

        modelMapper.map(editablePostAttributesDto, postEntity);

        assertEquals(postEntity0.getId(), postEntity.getId());
        assertEquals(postEntity0.getCreatedAt(), postEntity.getCreatedAt());
        assertEquals(editablePostAttributesDto.getTitle(), postEntity.getTitle());
        assertEquals(editablePostAttributesDto.getText(), postEntity.getText());
    }

    private static PostEntity.PostEntityBuilder postEntityBuilder(String seed) {
        return PostEntity.builder()
                .id("id" + seed)
                .createdAt(Instant.parse("2018-02-07T00:39:53Z"))
                .title("some title " + seed)
                .text("some text " + seed);
    }

    private static void assertPostDtoRepresentsPostEntity(PostEntity postEntity, PostDto postDto) {
        assertEquals(postEntity.getId(), postDto.getId());
        assertEquals(postEntity.getCreatedAt(), postDto.getCreatedAt());
        assertEquals(postEntity.getTitle(), postDto.getTitle());
        assertEquals(postEntity.getText(), postDto.getText());
    }

    private static void assertEditablePostAttributesDtoRepresentsPostEntity(
            PostEntity postEntity,
            EditablePostAttributesDto editablePostAttributesDto) {

        assertEquals(postEntity.getTitle(), editablePostAttributesDto.getTitle());
        assertEquals(postEntity.getText(), editablePostAttributesDto.getText());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PostEntity {
        private String id;
        private Instant createdAt;
        private String title;
        private String text;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(builderMethodName = "postBuilder")
    @EqualsAndHashCode(callSuper = true)
    public static class PostDto extends EditablePostAttributesDto {
        private String id;
        private Instant createdAt;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(builderMethodName = "editablePostAttributesBuilder")
    public static class EditablePostAttributesDto {
        private String title;
        private String text;
    }
}
