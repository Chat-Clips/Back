package com.example.chatClips.service;

import com.example.chatClips.domain.Chat;
import com.example.chatClips.domain.ChatRoom;
import com.example.chatClips.domain.User;
import com.example.chatClips.domain.mapping.UserChatRoom;
import com.example.chatClips.dto.ChatRoomRequest;
import com.example.chatClips.dto.CommandDTO;
import com.example.chatClips.dto.LoadChatDTO;
import com.example.chatClips.repository.ChatRoomRepository;
import com.example.chatClips.repository.UserChatRoomRepository;
import com.example.chatClips.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final UserRepository userRepository;
    public String create(String roomName){
        ChatRoom chatRoom = ChatRoom.builder()
            .roomId(UUID.randomUUID().toString())
            .roomName(roomName)
            .userCount(0L)
            .build();
        return chatRoomRepository.save(chatRoom).getRoomId();
    }


    public List<LoadChatDTO> loadChat(ChatRoomRequest.LoadChatRoom request){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(request.getRoomId());
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
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);
        User user = userRepository.findByUserId(userId);
        return user.getUsername();
    }

    public void deleteUser(String roomId, String userId){
        User user = userRepository.findByUserId(userId);
        UserChatRoom userChatRoom = userChatRoomRepository.findByUser(user);
        userChatRoomRepository.delete(userChatRoom);
    }
    public String exitChatting(String roomId){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId);
        List<CommandDTO> chatList = chatRoomRepository.getAllChat(chatRoom);
        String input = new String();
        for(int i = 0; i < chatList.size(); i++) {
            input += chatList.get(i).getUserName() + " : " + chatList.get(i).getChat() + '\n';
        }
        return input;
    }

//    public List<String> getUserList(String roomId){
//        List<String> list = new ArrayList<>();
//        ChatRoom chatRoom = chatRepository.findByRoomId(roomId);
//        chatRoom.getUserList().forEach((key, value) -> list.add(value));
//        return list;
//    }
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
