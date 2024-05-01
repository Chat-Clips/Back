package com.example.chatClips.domain;

import com.example.chatClips.domain.mapping.UserChatRoom;
import com.example.chatClips.dto.ChatDTO;
import com.example.chatClips.service.ChatService;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

@Data
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomId;
    private String roomName;
    private Long userCount;

    @OneToMany(mappedBy = "chatRoom")
    private List<UserChatRoom> userChatRoomList = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom")
    private List<Chat> chatList = new ArrayList<>();
}
