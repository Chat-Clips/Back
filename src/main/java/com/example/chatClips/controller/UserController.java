package com.example.chatClips.controller;

import com.example.chatClips.apiPayload.ApiResponse;
import com.example.chatClips.converter.UserConverter;
import com.example.chatClips.dto.UserRequestDTO;
import com.example.chatClips.dto.UserResponseDTO;
import com.example.chatClips.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    private ApiResponse<UserResponseDTO.JoinDTO> signup(@Valid @RequestBody UserRequestDTO.JoinDTO request){
        return ApiResponse.onSuccess(UserConverter.toJoinDTO(userService.signup(request)));
    }
}
