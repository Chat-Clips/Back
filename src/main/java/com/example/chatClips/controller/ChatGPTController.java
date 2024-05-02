package com.example.chatClips.controller;

import com.example.chatClips.dto.CompletionDto;
import com.example.chatClips.service.ChatGPTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.chatClips.dto.ChatCompletionDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/chatGpt")
public class ChatGPTController {
    private final ChatGPTService chatGPTService;

    public ChatGPTController(ChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }


    /**
     * |chatClips가 사용하는 거 | ChatCompletionDTO: ChatRequestDTO:를 이용하여 작동.
     *
     * [API] 최신 ChatGPT 프롬프트 명령어를 수행합니다. : gpt-4, gpt-4 turbo, gpt-3.5-turbo
     *
     * @param chatCompletionDTO
     * @return
     */
    @PostMapping("/prompt")
    public ResponseEntity<Map<String, Object>> selectPrompt(@RequestBody ChatCompletionDTO chatCompletionDTO) {
        log.debug("param :: " + chatCompletionDTO.toString());
        Map<String, Object> result = chatGPTService.prompt(chatCompletionDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * [API] Legacy ChatGPT 프롬프트 명령을 수행합니다. : gpt-3.5-turbo-instruct, babbage-002, davinci-002
     *
     * @param completionDto {}
     * @return ResponseEntity<Map < String, Object>>
     */
    @PostMapping("/legacyPrompt")
    public ResponseEntity<Map<String, Object>> selectLegacyPrompt(@RequestBody CompletionDto completionDto) {
        log.debug("param :: " + completionDto.toString());
        Map<String, Object> result = chatGPTService.legacyPrompt(completionDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
