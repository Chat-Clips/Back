package com.example.chatClips.converter;

import com.example.chatClips.domain.Chat;
import com.example.chatClips.domain.Comment;
import com.example.chatClips.dto.ChatRoomResponse;
import com.example.chatClips.dto.LoadChatDTO;
import java.util.List;

public class ChatRoomConverter {

    public static ChatRoomResponse.LoadChatList toLoadChatRoom(List<LoadChatDTO> chat){
        return ChatRoomResponse.LoadChatList.builder()
            .loadChats(chat)
            .build();
    }
}
