package io.agibalov.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BriefUserDto {
    private String id;
    private String name;
}
