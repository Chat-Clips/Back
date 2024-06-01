package com.example.chatClips.converter;

import com.example.chatClips.domain.Feedback;
import com.example.chatClips.dto.FeedbackRequest;
import com.example.chatClips.dto.FeedbackResponse;
import java.util.List;

public class FeedbackConverter {

    public static FeedbackResponse.PostDTO toPostDTO(Feedback feedback){
        return FeedbackResponse.PostDTO.builder()
            .id(feedback.getId())
            .createdAt(feedback.getCreatedAt())
            .build();
    }

    public static FeedbackResponse.UpdateDTO toUpdateDTO(Feedback feedback){
        return FeedbackResponse.UpdateDTO.builder()
            .title(feedback.getTitle())
            .text(feedback.getText())
            .build();
    }
    public static FeedbackResponse.DeleteDTO toDeleteDTO(Long id) {
        return FeedbackResponse.DeleteDTO.builder()
            .id(id)
            .build();
    }
    public static FeedbackResponse.GetAllDTO toGetAll(List<Feedback> feedbackList){
        return FeedbackResponse.GetAllDTO.builder()
            .feedbackList(feedbackList)
            .build();
    }
    public static FeedbackResponse.GetFeedbackDTO toGetFeedbackDTO(Feedback feedback){
        return FeedbackResponse.GetFeedbackDTO.builder()
            .id(feedback.getId())
            .title(feedback.getTitle())
            .text(feedback.getText())
            .user(feedback.getUser())
            .createdAt(feedback.getCreatedAt())
            .build();
    }
}
