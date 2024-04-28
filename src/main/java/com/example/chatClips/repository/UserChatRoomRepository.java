package com.example.chatClips.repository;

import com.example.chatClips.domain.User;
import com.example.chatClips.domain.mapping.UserChatRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {

    UserChatRoom findByUser(User user);
}
