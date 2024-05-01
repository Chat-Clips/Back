package com.example.chatClips.repository;

import com.example.chatClips.domain.ChatRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    public ChatRoom findByRoomId(String roomId);
}
