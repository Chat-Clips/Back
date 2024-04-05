package com.example.chatClips.controller;

import com.example.chatClips.apiPayload.ApiResponse;
import com.example.chatClips.converter.CommentConverter;
import com.example.chatClips.dto.CommentRequest;
import com.example.chatClips.dto.CommentResponse;
import com.example.chatClips.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/reply")
    private ApiResponse<CommentResponse.ReplyDTO> reply(@Valid @RequestBody CommentRequest.ReplyDTO request){
        return ApiResponse.onSuccess(CommentConverter.toReplyDTO(commentService.reply(request)));
    }
}
