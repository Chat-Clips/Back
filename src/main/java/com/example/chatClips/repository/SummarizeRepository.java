package com.example.chatClips.repository;

import com.example.chatClips.domain.ChatRoom;
import com.example.chatClips.domain.Summarize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SummarizeRepository extends JpaRepository<Summarize, Long> {

    Summarize findByChatRoom(ChatRoom chatRoom);
}
