package com.example.chatClips.converter;

import com.example.chatClips.domain.Chat;
import com.example.chatClips.domain.Comment;
import com.example.chatClips.dto.ChatRoomResponse;
import com.example.chatClips.dto.ChatRoomResponse.LoadChatRoomDTO;
import com.example.chatClips.dto.CommandDTO;
import com.example.chatClips.dto.CommentResponse;
import java.util.List;

public class ChatRoomConverter {

    public static ChatRoomResponse.LoadChat toLoadChatRoom(List<CommandDTO> chat){
        return ChatRoomResponse.LoadChat.builder()
            .loadChat(chat)
            .build();
    }
}
