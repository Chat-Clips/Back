package com.example.chatClips.converter;

import com.example.chatClips.domain.User;
import com.example.chatClips.dto.UserResponseDTO;

public class UserConverter {

    public static UserResponseDTO.JoinDTO toJoinDTO(User user){
        return UserResponseDTO.JoinDTO.builder()
            .id(user.getId())
            .createdAt(user.getCreatedAt())
            .build();
    }
}
