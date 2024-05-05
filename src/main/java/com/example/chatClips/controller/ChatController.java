package com.example.chatClips.controller;

import com.example.chatClips.domain.Chat;
import com.example.chatClips.domain.ChatRoom;
import com.example.chatClips.domain.User;
import com.example.chatClips.dto.ChatDTO;
import com.example.chatClips.repository.ChatRepository;
import com.example.chatClips.repository.ChatRoomRepository;
import com.example.chatClips.repository.UserChatRoomRepository;
import com.example.chatClips.repository.UserRepository;
import com.example.chatClips.service.ChatRoomService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations template;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;
    private final UserChatRoomRepository userChatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    @MessageMapping("/enterUser")
    public void enterUser(@Payload ChatDTO chat, SimpMessageHeaderAccessor headerAccessor){
        chatRoomService.increaseUser(chat.getRoomId());

        String userId = chatRoomService.addUser(chat.getRoomId(), chat.getSender());

        headerAccessor.getSessionAttributes().put("userId", userId);
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());
        User user = userRepository.findByUserId(userId);
        chat.setMessage(user.getUsername() + "님이 입장하셨습니다.");
        template.convertAndSend("/sub/chatroom/" + chat.getRoomId(), chat);
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload ChatDTO chat){
        chat.setMessage(chat.getMessage());
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(chat.getRoomId());
        User user = userRepository.findByUserId(chat.getSender());
        Chat chatting = Chat.builder()
            .chatRoom(chatRoom)
            .user(user)
            .chat(chat.getMessage())
            .time(LocalDateTime.now())
            .build();
        chatRepository.save(chatting);
        template.convertAndSend("/sub/chatroom/" + chat.getRoomId(), chat);
    }

    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        chatRoomService.decreaseUser(roomId);

        //채팅방 유저 리스트에서 UUID 유저 닉네임 조회 및 리스트에서 유저 삭제
        String userName = chatRoomService.getUserName(roomId, userId);
        chatRoomService.deleteUser(roomId,userId);

        if(userName != null){
            log.info("User Disconnected : " + userName);

            ChatDTO chat = ChatDTO.builder()
                .type(ChatDTO.MessageType.LEAVE)
                .sender(userId)
                .message(userName + "님이 퇴장하였습니다.")
                .build();

            template.convertAndSend("/sub/chatroom/" + roomId,chat);
        }
    }
//
//    // 채팅에 참여한 유저 리스트 반환
//    @GetMapping("/userlist")
//    @ResponseBody
//    public List<String> userList(String roomId){
//
//        return chatRoomService.getUserList(roomId);
//    }
//
//    // 채팅에 참여한 유저 닉네임 중복 확인
//    @GetMapping("/duplicateName")
//    @ResponseBody
//    public String isDuplicateName(@RequestParam("roomId")String roomId ,
//        @RequestParam("username")String username){
//
//        String userName = chatRoomService.isDuplicateName(roomId, username);
//        log.info("DuplicateName : {}", userName);
//
//        return userName;
//    }

}