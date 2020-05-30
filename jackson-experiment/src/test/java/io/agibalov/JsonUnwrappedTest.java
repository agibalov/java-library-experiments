package io.agibalov;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JsonUnwrappedTest {
    @Test
    public void dummy() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(ContainerDto.builder()
                .id("xxx")
                .userInfo(UserInfo.builder()
                        .userId("uuu")
                        .userName("user")
                        .build())
                .build());
        Map<String, Object> obj = objectMapper.readValue(json, Map.class);
        assertEquals(new HashMap<String, Object>() {{
            put("id", "xxx");
            put("userId", "uuu");
            put("userName", "user");
        }}, obj);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContainerDto {
        private String id;

        @JsonUnwrapped
        private UserInfo userInfo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private String userId;
        private String userName;
    }
}
