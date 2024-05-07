package com.example.chatClips.service;

import com.example.chatClips.domain.User;
import com.example.chatClips.dto.UserLoginDTO;
import com.example.chatClips.dto.UserRequestDTO;
import com.example.chatClips.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    public String signup(UserRequestDTO.JoinDTO request){
        // 입력된 userId로 이미 가입된 사용자가 있는지 확인
        if (userRepository.existsByUserId(request.getUserId())) {
            return "이미 존재하는 userId입니다.";
        }

        User user = User.builder()
            .userId(request.getUserId())
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role("ROLE_USER")
            .createdAt(LocalDateTime.now())
            .build();
        userRepository.save(user);
        return "회원가입이 완료되었습니다.";
    }
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }
    public User login(UserLoginDTO request) {
        User user = userRepository.findByUserId(request.getUserId());
        if (user != null) {
            if(passwordEncoder.matches(request.getPassword(), user.getPassword())){return user;}
            //if(user.getPassword().equals(request.getPassword())){return user;}
            else{return null;}
        } else {
            return null;
        }

    }

}
