package com.example.chatClips.controller;

import com.example.chatClips.apiPayload.ApiResponse;
import com.example.chatClips.converter.UserConverter;
import com.example.chatClips.domain.User;
import com.example.chatClips.dto.UserLoginDTO;
import com.example.chatClips.dto.UserRequestDTO;
import com.example.chatClips.dto.UserResponseDTO;
import com.example.chatClips.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.chatClips.domain.User.sessionList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    private ApiResponse<UserResponseDTO.JoinDTO> signup(@Valid @RequestBody UserRequestDTO.JoinDTO request){
        return ApiResponse.onSuccess(UserConverter.toJoinDTO(userService.signup(request)));
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, @RequestBody UserLoginDTO request, HttpServletRequest http) {
        User loginMember = userService.login(request);

        if(loginMember == null) {
            return "로그인 실패";
        }

        HttpSession session = http.getSession();
        session.setAttribute("UserId", loginMember.getUserId());
        sessionList.put(session.getId(), session);
        session.setMaxInactiveInterval(18000); //5시간
        return "로그인 성공";
    }
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session == null) {
            return null;
        }
        sessionList.remove(session.getId());
        session.invalidate();
        return "로그아웃";
    }
    @GetMapping("/session-list")
    @ResponseBody
    public Map<String, String> sessionList() {
        Enumeration elements = sessionList.elements();
        Map<String, String> lists = new HashMap<>();
        while(elements.hasMoreElements()) {
            HttpSession session = (HttpSession)elements.nextElement();
            lists.put(session.getId(), String.valueOf(session.getAttribute("UsernId")));
        }
        return lists;
    }
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/id/{userId}")
    public User getUserById(@PathVariable String userId) {
        return userService.findByUserId(userId);
    }

}
