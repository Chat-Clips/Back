package com.example.chatClips.repository;

import com.example.chatClips.domain.ChatRoom;
import com.example.chatClips.dto.CommandDTO;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    public ChatRoom findByRoomId(String roomId);

    @Query(value = "select new com.example.chatClips.dto.CommandDTO(user.username, chat.chat) from User user inner join Chat chat on chat.user = user where chat.chatRoom = :chatRoom order by chat.time")
    public List<CommandDTO> getAllChat(@Param("chatRoom") ChatRoom chatRoom);

}
