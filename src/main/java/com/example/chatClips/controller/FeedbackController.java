package com.example.chatClips.controller;

import com.example.chatClips.apiPayload.ApiResponse;
import com.example.chatClips.converter.FeedbackConverter;
import com.example.chatClips.converter.UserConverter;
import com.example.chatClips.domain.Feedback;
import com.example.chatClips.dto.*;
import com.example.chatClips.dto.FeedbackResponse.DeleteDTO;
import com.example.chatClips.dto.UserResponseDTO.JoinDTO;
import com.example.chatClips.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    @PostMapping("/post")
    private ApiResponse<FeedbackResponse.PostDTO> post(@Valid @RequestBody FeedbackRequest.PostDTO request){
        return ApiResponse.onSuccess(FeedbackConverter.toPostDTO(feedbackService.post(request)));
    }

    @PostMapping("/update")
    public ApiResponse<FeedbackResponse.UpdateDTO> updateFeedback(@Valid @RequestBody FeedbackRequest.UpdateDTO request) {
        return ApiResponse.onSuccess(FeedbackConverter.toUpdateDTO(feedbackService.update(request)));
    }

    @DeleteMapping("/delete/{userId}")
    public ApiResponse<DeleteDTO> deleteFeedback(@PathVariable("userId") String userId) {
        return ApiResponse.onSuccess(FeedbackConverter.toDeleteDTO(feedbackService.delete(userId)));
    }

    @GetMapping("/all")
    public ApiResponse<FeedbackResponse.GetAllDTO> getAllPosts() {
        return ApiResponse.onSuccess(FeedbackConverter.toGetAll(feedbackService.getAllPosts()));
    }
    @GetMapping("feedback/{id}")
    public ApiResponse<FeedbackResponse.GetFeedbackDTO> getFeedback(@PathVariable("id") Long id){
        return ApiResponse.onSuccess(FeedbackConverter.toGetFeedbackDTO(feedbackService.getFeedback(id)));
    }
}
