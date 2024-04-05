package com.example.chatClips.service;

import com.example.chatClips.apiPayload.code.status.ErrorStatus;
import com.example.chatClips.apiPayload.exception.handler.FeedbackHandler;
import com.example.chatClips.apiPayload.exception.handler.UserHandler;
import com.example.chatClips.domain.Comment;
import com.example.chatClips.domain.Feedback;
import com.example.chatClips.domain.User;
import com.example.chatClips.dto.CommentRequest;
import com.example.chatClips.repository.CommentRepository;
import com.example.chatClips.repository.FeedbackRepository;
import com.example.chatClips.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final CommentRepository commentRepository;

    public Comment reply(CommentRequest.ReplyDTO request){
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Feedback feedback = feedbackRepository.findById(request.getFeedbackId()).orElseThrow(() -> new FeedbackHandler(ErrorStatus.FEEDBACK_NOT_FOUND));
        Comment comment = Comment.builder()
            .text(request.getText())
            .user(user)
            .feedback(feedback)
            .createdAt(LocalDateTime.now())
            .build();
        return commentRepository.save(comment);
    }
}
