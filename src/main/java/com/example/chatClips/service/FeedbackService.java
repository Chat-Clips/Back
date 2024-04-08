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

        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Feedback feedback = Feedback.builder()
            .title(request.getTitle())
            .text(request.getText())
            .user(user)
            .createdAt(LocalDateTime.now())
            .build();
        return feedbackRepository.save(feedback);
    }

    public Feedback update(FeedbackRequest.UpdateDTO request){
        Feedback feedback = feedbackRepository.findById(request.getId()).orElseThrow(() -> new FeedbackHandler(ErrorStatus.FEEDBACK_NOT_FOUND));

        // 피드백 내용을 업데이트합니다.
        feedback.setTitle(request.getTitle());
        feedback.setText(request.getText());

        // 업데이트된 피드백을 저장합니다.
        return feedbackRepository.save(feedback);
    }

    public void deletePost(Long Id) {
        feedbackRepository.deleteById(Id);
    }

    public List<Feedback> getAllPosts() {    //게시글 목록 조회
       return feedbackRepository.findAll();
    }
}
