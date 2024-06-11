package com.example.chatClips.controller;

import com.example.chatClips.apiPayload.ApiResponse;
import com.example.chatClips.dto.*;
import com.example.chatClips.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

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

    @Value("${openai.moderation.url}")
    private String moderationURL;

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate template;
    private final EmbeddingService embeddingService;

    @PostMapping("/chat")
    public ApiResponse<ChatgptApiResponse.SendMessageResultDTO> chat(@RequestBody ChatgptApiRequest.SendMessageDTO userPrompt) {
        try {
            // Moderation API 호출
            Map<String, String> moderationRequest = Map.of("input", userPrompt.getMessage());
            Map<String, Object> moderationResponse = template.postForObject(moderationURL, moderationRequest, Map.class);

            if (moderationResponse == null || moderationResponse.get("results") == null) {
                return ApiResponse.onFailure("500", "Moderation API 응답 오류", null);
            }

            // Moderation API 응답 확인
            List<Map<String, Object>> results = (List<Map<String, Object>>) moderationResponse.get("results");
            boolean flagged = results.stream().anyMatch(result -> {
                boolean resultFlagged = (boolean) result.get("flagged");
                Map<String, Double> categories = (Map<String, Double>) result.get("category_scores");
                double hateScore = categories.getOrDefault("hate", 0.0);
                double violenceScore = categories.getOrDefault("violence", 0.0);
                double selfHarmScore = categories.getOrDefault("self-harm", 0.0);
                // Adjust the sensitivity threshold as needed
                return resultFlagged || hateScore > 0.002 || violenceScore > 0.04 || selfHarmScore > 0.01;
            });
            if (flagged) {
                return ApiResponse.onFailure("400", "대화 내용에 유해성 발언이 포함되어 있으므로 요약기능 및 사이트 추천기능을 제공하지 않습니다.", null);
            }

            String systemPrompt = "You are an NLP that summarizes the contents of the meeting. If students finish chatting, you should just summarize based on the contents of the chat. However, the contents of the chat are related to computer science, so you have to think about it when processing. The maximum number of printouts can be up to 200 characters. You have to print them out in Korean. Next, recommendations must be made only when the chat conversation is related to computers or development. At the end, provide two site links that you can refer to related to the chat conversation when the chat summary is only related to computers or development. Please recommend official and blog-oriented links as the recommended links.";

            ChatGPTRequest request = new ChatGPTRequest(model, systemPrompt, userPrompt.getMessage());
            ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, request, ChatGPTResponse.class);

            if (chatGPTResponse == null || chatGPTResponse.getChoices().isEmpty()) {
                return ApiResponse.onFailure("500", "Failed to get a response from the ChatGPT API", null);
            }

            String summary = chatGPTResponse.getChoices().get(0).getMessage().getContent();
            List<String> recommendedSites = embeddingService.recommendSites(summary);

            return ApiResponse.onSuccess(ChatgptApiResponse.SendMessageResultDTO.builder()
                .message(summary)
                .recommemdedSites(recommendedSites)
                .build()
            );
        } catch (HttpClientErrorException e) {
            log.error("API call failed: {}", e.getMessage());
            return ApiResponse.onFailure("500", "Internal server error", null);
        } catch (Exception e) {
            log.error("Unexpected error occurred: {}", e.getMessage());
            return ApiResponse.onFailure("500", "An unexpected error occurred", null);
        }
    }
}
