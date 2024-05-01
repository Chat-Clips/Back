package com.example.chatClips.converter;

import com.example.chatClips.domain.Comment;
import com.example.chatClips.domain.Feedback;
import com.example.chatClips.dto.CommentResponse;
import com.example.chatClips.dto.FeedbackResponse;

public class CommentConverter {

    public static CommentResponse.ReplyDTO toReplyDTO(Comment comment){
        return CommentResponse.ReplyDTO.builder()
            .id(comment.getId())
            .createdAt(comment.getCreatedAt())
            .build();
    }

}
