package com.example.chatClips.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatCompletionDTO {//여기가 요청 보내는 베이스라고 생각하면 돼!

    // 사용할 모델
    private String model;

    private List<ChatRequestMsgDTO> messages;

//    @JsonProperty("max_tokens")
//    private Integer maxTokens;
//    private Double temperature;
//    @JsonProperty("top_p")
//    private Double topP;

    @Builder
    public ChatCompletionDTO(String model, List<ChatRequestMsgDTO> messages) {
        this.model = model;
        this.messages = messages;
    }
}