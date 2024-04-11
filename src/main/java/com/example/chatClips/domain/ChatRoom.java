package com.example.chatClips.domain;

import com.example.chatClips.dto.ChatDTO;
import com.example.chatClips.service.ChatService;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

@Data
public class ChatRoom {

    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name){
        this.roomId = roomId;
        this.name = name;
    }

    public void handleAction(WebSocketSession session, ChatDTO message, ChatService service){
        if(message.getType().equals(ChatDTO.MessageType.ENTER)){
            sessions.add(session);

            message.setMessage(message.getSender() + "님이 입장하였습니다.");
            sendMessage(message, service);
        } else if (message.getType().equals(ChatDTO.MessageType.TALK)){
            message.setMessage(message.getMessage());
            sendMessage(message, service);
        }
    }
    public <T> void sendMessage(T message, ChatService service){
        sessions.parallelStream().forEach(sessions -> service.sendMessage(sessions, message));
    }
}
