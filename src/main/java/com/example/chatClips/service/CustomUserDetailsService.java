package com.example.chatClips.service;

import com.example.chatClips.domain.User;
import com.example.chatClips.dto.CustomUserDetails;
import com.example.chatClips.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        User userData = userRepository.findByUserId(userId);
        if(userData != null) {
            System.out.println("success");
            return new CustomUserDetails(userData);
        }
        System.out.println("fail");
        return null;
    }
}