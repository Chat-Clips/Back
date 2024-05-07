package com.example.chatClips.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoadChatDTO {

    private String userName;
    private String chat;
    private LocalDateTime sendTime;
}
