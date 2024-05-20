package com.example.chatClips.repository;

import com.example.chatClips.domain.ChatRoom;
import com.example.chatClips.domain.User;
import com.example.chatClips.domain.mapping.UserChatRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {

    public UserChatRoom findByUserAndChatRoom(User user, ChatRoom chatRoom);
    @Query("""
    select userChatRoom.user
    from UserChatRoom userChatRoom
    where userChatRoom.chatRoom = :chatRoom
    """)
    public List<User> findByChatRoom(@Param("chatRoom") ChatRoom chatRoom);
}
