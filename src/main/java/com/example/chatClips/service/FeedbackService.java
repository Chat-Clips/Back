package com.example.chatClips.service;

import com.example.chatClips.apiPayload.code.status.ErrorStatus;
import com.example.chatClips.apiPayload.exception.handler.UserHandler;
import com.example.chatClips.domain.Feedback;
import com.example.chatClips.domain.User;
import com.example.chatClips.dto.FeedbackRequest;
import com.example.chatClips.repository.FeedbackRepository;
import com.example.chatClips.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    public Feedback post(FeedbackRequest.PostDTO request){

        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Feedback feedback = Feedback.builder()
            .title(request.getTitle())
            .text(request.getText())
            .user(user)
            .createdAt(LocalDateTime.now())
            .build();
        return feedbackRepository.save(feedback);
    }
}
