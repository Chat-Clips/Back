package com.example.chatClips.converter;

import com.example.chatClips.domain.Feedback;
import com.example.chatClips.dto.FeedbackResponse;
import java.util.List;

public class FeedbackConverter {

    public static FeedbackResponse.PostDTO toPostDTO(Feedback feedback){
        return FeedbackResponse.PostDTO.builder()
            .id(feedback.getId())
            .createdAt(feedback.getCreatedAt())
            .build();
    }
    public static FeedbackResponse.GetAllDTO toGetAll(List<Feedback> feedbackList){
        return FeedbackResponse.GetAllDTO.builder()
            .feedbackList(feedbackList)
            .build();
    }
}
