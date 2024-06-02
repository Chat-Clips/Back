package com.example.chatClips.controller;

import com.example.chatClips.apiPayload.ApiResponse;
import com.example.chatClips.dto.ChatGPTRequest;
import com.example.chatClips.dto.ChatGPTResponse;
import com.example.chatClips.dto.ChatgptApiRequest;
import com.example.chatClips.dto.ChatgptApiResponse;
import com.example.chatClips.dto.ChatgptApiResponse.SendMessageResultDTO;
import com.example.chatClips.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;


import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api")
public class ChatGPTController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;
    private final EmbeddingService embeddingService;

    @PostMapping("/chat")
    public ApiResponse<SendMessageResultDTO> chat(@RequestBody ChatgptApiRequest.SendMessageDTO userPrompt) {
        String systemPrompt = "You are an nlp that summarizes the contents of the meeting. If students finish chatting after chatting, you should just summarize based on the contents of the chat. However, the contents of the chat are related to computer science, so you have to think about it when processing. However, the maximum number of printouts can be up to 200 characters. And you have to print them out in Korean. Next recommendations must be made only please when the chat conservation is related to computer, development. At the end, provide two site links that you can refer to related to the chat conversation when chat summary is only related to computer, development. Please recommend official and blog-oriented links as the recommended links.";
        ChatGPTRequest request = new ChatGPTRequest(model, systemPrompt, userPrompt.getMessage());
        ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, request, ChatGPTResponse.class);
        String summary = chatGPTResponse.getChoices().get(0).getMessage().getContent();
        List<String> recommendedSites = embeddingService.recommendSites(summary);

        return ApiResponse.onSuccess(ChatgptApiResponse.SendMessageResultDTO.builder()
                .message(chatGPTResponse.getChoices().get(0).getMessage().getContent())
                .recommemdedSites(recommendedSites)
                .build()
        );
    }

}
