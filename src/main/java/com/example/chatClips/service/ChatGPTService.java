package com.example.chatClips.service;

import com.example.chatClips.dto.ChatCompletionDTO;

import java.util.Map;

public interface ChatGPTService {
    Map<String, Object> prompt(ChatCompletionDTO chatCompletionDTO);
}
