package com.example.chatClips.service;

import com.example.chatClips.apiPayload.code.status.ErrorStatus;
import com.example.chatClips.apiPayload.exception.handler.FeedbackHandler;
import com.example.chatClips.apiPayload.exception.handler.UserHandler;
import com.example.chatClips.domain.Feedback;
import com.example.chatClips.domain.User;
import com.example.chatClips.dto.FeedbackRequest;
import com.example.chatClips.repository.FeedbackRepository;
import com.example.chatClips.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    public Feedback post(FeedbackRequest.PostDTO request){

        User user = userRepository.findByUserId(request.getUserId());
        Feedback feedback = Feedback.builder()
            .title(request.getTitle())
            .text(request.getText())
            .user(user)
            .createdAt(LocalDateTime.now())
            .build();
        return feedbackRepository.save(feedback);
    }

    public Feedback update(FeedbackRequest.UpdateDTO request){
        User user = userRepository.findByUserId(request.getUserId());
        Feedback feedback = feedbackRepository.findById(user.getId()).orElseThrow(() -> new FeedbackHandler(ErrorStatus.FEEDBACK_NOT_FOUND));

        // 피드백 내용을 업데이트합니다.
        feedback.setTitle(request.getTitle());
        feedback.setText(request.getText());

        // 업데이트된 피드백을 저장합니다.
        return feedbackRepository.save(feedback);
    }

    public Long delete(String userId) {
        User user = userRepository.findByUserId(userId);
        Feedback feedback = feedbackRepository.findById(user.getId()).orElseThrow(() -> new FeedbackHandler(ErrorStatus.FEEDBACK_NOT_FOUND));
        feedbackRepository.delete(feedback);
        return feedback.getId();
    }

    public List<Feedback> getAllPosts() {    //게시글 목록 조회
       return feedbackRepository.findAll();
    }

    public Feedback getFeedback(Long id) {
        return feedbackRepository.findById(id).orElseThrow(() -> new FeedbackHandler(ErrorStatus.FEEDBACK_NOT_FOUND));
    }
}
