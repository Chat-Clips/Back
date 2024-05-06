package com.example.chatClips.service;

import com.example.chatClips.dto.ChatCompletionDTO;
import com.example.chatClips.dto.CompletionDto;

import java.util.Map;

public interface ChatGPTService {
    Map<String, Object> legacyPrompt(CompletionDto completionDto);
    Map<String, Object> prompt(ChatCompletionDTO chatCompletionDTO);
}
