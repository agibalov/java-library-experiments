package io.agibalov.service;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class PostDto {
    private String id;
    private String title;
    private String text;
    private Instant createdAt;
    private int commentCount;
    private BriefUserDto author;
}
