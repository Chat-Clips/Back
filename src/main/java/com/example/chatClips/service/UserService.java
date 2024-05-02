package com.example.chatClips.service;

import com.example.chatClips.domain.User;
import com.example.chatClips.dto.UserLoginDTO;
import com.example.chatClips.dto.UserRequestDTO;
import com.example.chatClips.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;

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
            .password(request.getPassword())
            .createdAt(LocalDateTime.now())
            .build();
        return userRepository.save(user);
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public User login(UserLoginDTO request) {
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (user != null) {
            if(user.getPassword().equals(request.getPassword())){return user;}
            else{return null;}
        } else {
            return null;
        }

    }

}
