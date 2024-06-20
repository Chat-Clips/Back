package com.example.chatClips.service;

import com.example.chatClips.domain.Chat;
import com.example.chatClips.domain.ChatRoom;
import com.example.chatClips.domain.User;
import com.example.chatClips.domain.mapping.UserChatRoom;
import com.example.chatClips.dto.ChatDTO;
import com.example.chatClips.dto.CommandDTO;
import com.example.chatClips.dto.LoadChatDTO;
import com.example.chatClips.repository.ChatRepository;
import com.example.chatClips.repository.ChatRoomRepository;
import com.example.chatClips.repository.UserChatRoomRepository;
import com.example.chatClips.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final UserRepository userRepository;
    private final SimpMessageSendingOperations template;
    private final ChatRepository chatRepository;

    public String create(String roomName){
        ChatRoom chatRoom = ChatRoom.builder()
            .roomId(UUID.randomUUID().toString())
            .roomName(roomName)
            .userCount(0L)
            .isTerminated(false)
            .build();
        return chatRoomRepository.save(chatRoom).getRoomId();
    }


    public void enterUser(ChatDTO chat, SimpMessageHeaderAccessor headerAccessor){
        String userId = addUser(chat.getRoomId(), chat.getSender());

        headerAccessor.getSessionAttributes().put("userId", userId);
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());
        for(Map.Entry<String, Object> entry: headerAccessor.getSessionAttributes().entrySet()){
            System.out.println("first: "+entry.getKey()+ "second:"+entry.getValue()+"\n");
        }
        User user = userRepository.findByUserId(userId);
        chat.setMessage(user.getUsername() + "님이 입장하셨습니다.");
        template.convertAndSend("/sub/chatroom/" + chat.getRoomId(), chat);
    }

    public void sendMessage(ChatDTO chat){
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

    public void exit(StompHeaderAccessor headerAccessor){

        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        //chatRoomService.decreaseUser(roomId);
        //채팅방 유저 리스트에서 UUID 유저 닉네임 조회 및 리스트에서 유저 삭제
        String userName = getUserName(roomId, userId);
        //deleteUser(roomId,userId);

        if(userName != null){
            log.info("User Disconnected : " + userName);

        }
    }
    public void terminateRoom(String roomId){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);
        chatRoom.setIsTerminated(true);
        chatRoomRepository.save(chatRoom);
    }
    public List<LoadChatDTO> loadChat(String roomId){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);
        return chatRoomRepository.getPrevChat(chatRoom);

    }
    public void increaseUser(String roomId){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);
        chatRoom.setUserCount(chatRoom.getUserCount() + 1);
        chatRoomRepository.save(chatRoom);
    }

    public void decreaseUser(String roomId){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);
        chatRoom.setUserCount(chatRoom.getUserCount() - 1);
        chatRoomRepository.save(chatRoom);
    }

    public String addUser(String roomId, String userId){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);
        User user = userRepository.findByUserId(userId);
        UserChatRoom userChatRoom = UserChatRoom.builder()
            .user(user)
            .chatRoom(chatRoom)
            .build();
        userChatRoomRepository.save(userChatRoom);
        return user.getUserId();
    }
    public String getUserName(String roomId,String userId){
        System.out.println("이름: "+ userId);
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);
        User user = userRepository.findByUserId(userId);
        return user.getUsername();
    }

    public void deleteUser(String roomId, String userId){
        User user = userRepository.findByUserId(userId);
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);
        UserChatRoom userChatRoom = userChatRoomRepository.findByUserAndChatRoom(user, chatRoom);
        userChatRoomRepository.delete(userChatRoom);
    }
    public String exitChatting(String roomId){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);
        chatRoom.setIsTerminated(true);
        chatRoomRepository.save(chatRoom);
        List<CommandDTO> chatList = chatRoomRepository.getAllChat(chatRoom);
        String input = new String();
        for(int i = 0; i < chatList.size(); i++) {
            input += chatList.get(i).getUserName() + " : " + chatList.get(i).getChat() + '\n';
        }
        return input;
    }

    public List<User> getUserList(String roomId){
        List<String> list = new ArrayList<>();
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);
        return userChatRoomRepository.findByChatRoom(chatRoom);
    }
    public boolean isTerminate(String roomId){
        return chatRoomRepository.findByRoomId(roomId).getIsTerminated();
    }
//
//    public String isDuplicateName(String roomId, String username){
//        ChatRoom chatRoom = chatRepository.findByRoomId(roomId);
//        String temp = username;
//        while(chatRoom.getUserList().containsValue(temp)){
//            int ranNum = (int) (Math.random() * 100) + 1;
//            temp = username+ranNum;
//        }
//        return temp;
//    }
}
