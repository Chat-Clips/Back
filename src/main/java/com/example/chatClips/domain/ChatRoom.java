package com.example.chatClips.domain;

import com.example.chatClips.domain.mapping.UserChatRoom;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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
    private Boolean isTerminated;
    @OneToMany(mappedBy = "chatRoom")
    private List<UserChatRoom> userChatRoomList = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom")
    private List<Chat> chatList = new ArrayList<>();

    @OneToOne(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Summarize summarize;
}
