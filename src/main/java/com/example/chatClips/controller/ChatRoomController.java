package com.example.chatClips.controller;

import com.example.chatClips.apiPayload.ApiResponse;
import com.example.chatClips.converter.ChatRoomConverter;
import com.example.chatClips.domain.ChatRoom;
import com.example.chatClips.domain.User;
import com.example.chatClips.domain.mapping.UserChatRoom;
import com.example.chatClips.dto.ChatRoomResponse;
import com.example.chatClips.dto.ChatRoomResponse.LoadChatList;
import com.example.chatClips.dto.ChatRoomResponse.UserChatRoomDTO;
import com.example.chatClips.repository.ChatRoomRepository;
import com.example.chatClips.repository.UserRepository;
import com.example.chatClips.service.ChatRoomService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;

    @GetMapping("/loadChatting")
    public ApiResponse<LoadChatList> loadChatting(@Valid @RequestParam String roomId) {
        return ApiResponse.onSuccess(ChatRoomConverter.toLoadChatRoom(chatRoomService.loadChat(roomId)));
    }

    // chatRoom 리스트 반환
    @GetMapping("")
    public List<ChatRoomResponse.UserChatRoomDTO> ChatRoomList(@RequestParam String userId){
        User user = userRepository.findByUserId(userId);
        List<UserChatRoom> userChatRoomList = user.getUserChatRoomList();
        List<ChatRoomResponse.UserChatRoomDTO> chatRoomList = new ArrayList<>();
        for (UserChatRoom userChatRoom : userChatRoomList){
            chatRoomList.add(UserChatRoomDTO.builder().roomId(userChatRoom.getChatRoom().getRoomId()).roomName(userChatRoom.getChatRoom().getRoomName()).build());
        }
        return chatRoomList;
    }

    //채팅방 생성
    @PostMapping("/createRoom")
    public String createRoom(@RequestParam String roomName){
        return chatRoomService.create(roomName);
    }

    //채팅방 입장
    @GetMapping("/joinroom")
    public ChatRoom joinRoom(String roomId){
        return chatRoomRepository.findByRoomId(roomId);
    }

    //채팅방 종료시 요약
    @GetMapping("/exitChatting")
    public String exitChatting(@RequestParam String roomId){
        return chatRoomService.exitChatting(roomId);
    }
}
