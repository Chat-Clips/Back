package com.example.chatClips.service;

import com.example.chatClips.apiPayload.code.status.ErrorStatus;
import com.example.chatClips.apiPayload.exception.handler.FeedbackHandler;
import com.example.chatClips.apiPayload.exception.handler.UserHandler;
import com.example.chatClips.domain.ChatRoom;
import com.example.chatClips.domain.Comment;
import com.example.chatClips.domain.Feedback;
import com.example.chatClips.domain.Summarize;
import com.example.chatClips.domain.User;
import com.example.chatClips.dto.CommentRequest;
import com.example.chatClips.dto.SummarizeRequestDTO;
import com.example.chatClips.dto.SummarizeResponseDTO;
import com.example.chatClips.repository.ChatRoomRepository;
import com.example.chatClips.repository.SummarizeRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SummarizeService {
    private final SummarizeRepository summarizeRepository;
    private final ChatRoomRepository chatRoomRepository;
    public Long getIdFromRoomId(String roomId){
        return chatRoomRepository.findByRoomId(roomId).getId();
    }
    public Summarize summary(String roomId){
        ChatRoom chatRoom = chatRoomRepository.findById(getIdFromRoomId(roomId)).orElseThrow();
        System.out.println(chatRoom.getRoomId());
        return summarizeRepository.findByChatRoom(chatRoom);
    }
    public Summarize save(SummarizeRequestDTO.Save request){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(request.getRoomId());
        Summarize summarize = Summarize.builder()
            .chatRoom(chatRoom)
            .summary(request.getSummary())
            .build();
        return summarizeRepository.save(summarize);
    }

}
