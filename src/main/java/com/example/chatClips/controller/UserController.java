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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;



@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    public static Hashtable sessionList = new Hashtable();

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserRequestDTO.JoinDTO request) {
        String message = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, @RequestBody UserLoginDTO request, HttpServletRequest http) {
        User loginMember = userService.login(request);

        if(loginMember == null) {
            return "로그인 실패";
        }
//        // 이미 세션이 존재하는지 확인
//        HttpSession existingSession = findExistingSession(loginMember.getUserId());
//        if (existingSession != null) {
//            return "이미 로그인된 사용자입니다";
//        }
        //http.getSession().invalidate();
        HttpSession session = http.getSession();
        session.setAttribute("UserId", loginMember.getUserId());
        sessionList.put(session.getId(), session);
        session.setMaxInactiveInterval(18000); //5시간
        return "로그인 성공";
    }

    private HttpSession findExistingSession(String userId) {
        Enumeration<HttpSession> sessions = sessionList.elements();
        while (sessions.hasMoreElements()) {
            HttpSession session = sessions.nextElement();
            String sessionUserId = (String) session.getAttribute("UserId");
            if (sessionUserId != null && sessionUserId.equals(userId)) {
                return session;
            }
        }
        return null;
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
    @GetMapping("/profile")
    public ResponseEntity<String> getUserProfile(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 현재 요청의 세션을 가져옵니다.
        if (session != null && session.getAttribute("UserId") != null) {
            String userId = (String) session.getAttribute("UserId");
            // 여기서 userId를 사용하여 사용자 프로필을 가져오거나 처리합니다.
            return ResponseEntity.ok(userId);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
    }
    @GetMapping("/session-list")
    @ResponseBody
    public Map<String, String> sessionList() {
        Enumeration elements = sessionList.elements();
        Map<String, String> lists = new HashMap<>();
        while(elements.hasMoreElements()) {
            HttpSession session = (HttpSession)elements.nextElement();
            lists.put(session.getId(), String.valueOf(session.getAttribute("UserId")));
        }
        return lists;
    }
    @GetMapping("/{userId}")
    public ApiResponse<UserResponseDTO.FindUserDTO> getUserByUsername(@PathVariable String userId) {
        return  ApiResponse.onSuccess(UserConverter.toFindUser(userService.findByUserId(userId)));
    }


}
