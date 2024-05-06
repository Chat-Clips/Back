package com.example.chatClips.dto;

import lombok.*;


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class ChatRequestMsgDTO {//이 객체를 모아서 ChatCompletionDTO로 보내는 것임. 여기에 chat내용이 들어가도록 하면 될거 같기도
    private String role;

    private String content;

    @Builder
    public ChatRequestMsgDTO(String role, String content) {
        this.role = role;
        this.content = content;
    }
}