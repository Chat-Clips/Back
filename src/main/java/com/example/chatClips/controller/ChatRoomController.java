package com.example.chatClips.controller;

import com.example.chatClips.domain.ChatRoom;
import com.example.chatClips.repository.ChatRoomRepository;
import com.example.chatClips.service.ChatRoomService;
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

    // chatRoom 리스트 반환
    @GetMapping("")
    public List<ChatRoom> ChatRoomList(){
        return chatRoomRepository.findAll();
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
}
