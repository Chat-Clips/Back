package com.example.chatClips.dto;

import com.example.chatClips.domain.Chat;
import com.example.chatClips.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatRoomResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserChatRoomDTO{
        private String roomId;
        private String roomName;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoadChatRoomDTO{
        private String username;
        private String chat;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoadChat{
        private List<CommandDTO> loadChat;
    }
}
