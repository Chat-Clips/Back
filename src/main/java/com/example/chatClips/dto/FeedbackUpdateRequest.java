package com.example.chatClips.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FeedbackUpdateRequest {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDTO {
        private Long id;
        private String title;
        private String text;

    }
}
