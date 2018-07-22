package io.agibalov.service;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserDto {
    private String id;
    private String name;
    private Instant createdAt;
    private int postCount;
    private int commentCount;
}
