package com.example.chatClips.controller;

import com.example.chatClips.apiPayload.ApiResponse;
import com.example.chatClips.converter.FeedbackConverter;
import com.example.chatClips.converter.UserConverter;
import com.example.chatClips.dto.FeedbackRequest;
import com.example.chatClips.dto.FeedbackResponse;
import com.example.chatClips.dto.UserRequestDTO;
import com.example.chatClips.dto.UserResponseDTO;
import com.example.chatClips.dto.UserResponseDTO.JoinDTO;
import com.example.chatClips.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    @PostMapping("/post")
    private ApiResponse<FeedbackResponse.PostDTO> post(@Valid @RequestBody FeedbackRequest.PostDTO request){
        return ApiResponse.onSuccess(FeedbackConverter.toPostDTO(feedbackService.post(request)));
    }
}
