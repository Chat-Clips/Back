package com.example.chatClips.controller;

import com.example.chatClips.apiPayload.ApiResponse;
import com.example.chatClips.converter.FeedbackConverter;
import com.example.chatClips.converter.UserConverter;
import com.example.chatClips.domain.Feedback;
import com.example.chatClips.dto.*;
import com.example.chatClips.dto.UserResponseDTO.JoinDTO;
import com.example.chatClips.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    @PostMapping("/post")
    private ApiResponse<FeedbackResponse.PostDTO> post(@Valid @RequestBody FeedbackRequest.PostDTO request){
        return ApiResponse.onSuccess(FeedbackConverter.toPostDTO(feedbackService.post(request)));
    }

    @PostMapping("/update")
    public ApiResponse<String> updateFeedback(@Valid @RequestBody FeedbackUpdateRequest.PostDTO request) {
        feedbackService.FeedbackUpdate(request);
        return ApiResponse.onSuccess("피드백 업데이트가 성공적으로 수행되었습니다.");
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteFeedback(@PathVariable("id") Long id) {
        try {
            feedbackService.deletePost(id);
            return ApiResponse.onSuccess("피드백 삭제가 성공적으로 수행되었습니다.");
        } catch (Exception e) {
            return ApiResponse.onFailure("DELETE_FAILED", "피드백 삭제 중 오류가 발생했습니다: " + e.getMessage(), null);
        }
    }

    @GetMapping("/all")
    public List<Feedback> getAllPosts() {
        return feedbackService.getAllPosts();
    }
}
