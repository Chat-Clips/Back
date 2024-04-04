package com.example.chatClips.service;

import com.example.chatClips.domain.User;
import com.example.chatClips.dto.UserRequestDTO;
import com.example.chatClips.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User signup(UserRequestDTO.JoinDTO request){
        User user = User.builder()
            .userId(request.getUserId())
            .username(request.getUsername())
            .createdAt(LocalDateTime.now())
            .build();
        return userRepository.save(user);
    }
}
