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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;


import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
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

    @Autowired
    private RestTemplate template;
    private final EmbeddingService embeddingService;

    @PostMapping("/chat")
    public ApiResponse<SendMessageResultDTO> chat(
        @RequestBody ChatgptApiRequest.SendMessageDTO userPrompt) {
        // Moderation API 호출
        Map<String, String> moderationRequest = Map.of("input", userPrompt.getMessage());
        Map<String, Object> moderationResponse = template.postForObject(moderationURL,
            moderationRequest, Map.class);

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
            return ApiResponse.onSuccess(ChatgptApiResponse.SendMessageResultDTO.builder()
                .message("대화 내용에 유해성 발언이 포함되어 있으므로 요약기능 및 사이트 추천기능을 제공하지 않습니다.")
                .recommemdedSites(null)
                .build());
        }
        String systemPrompt = """
               1. Mission Goal and Context Guidelines:
               - You are an NLP that summarizes the contents of the meeting. After students finish chatting, summarize the chat contents based on the chat. Focus on computer science topics, but the maximum summary length is 200 characters. Write in Korean. Provide recommendations only if the chat is related to computer science or development. At the end, provide two relevant site links (both official) if related to computer science. If not related to computer science nor development, do not recommend any sites.
               
               2. Summary Constraints:
               - Summarize based on the chat contents related to computer science or development.
               - The summary should be detailed yet concise.
               - Use formal language in Korean. Always respond in Korean. ex) ~입니다, ~합니다, ~습니다 etc.
               - The summary should not exceed 200 characters.
               
               3. Topic Case Classification
               - If the core topic of the conversation is not CS, it is classified as case 1 and no recommended link is provided.
               - If the core topic of the conversation is CS, it is classified as case 2 and a recommended link is provided.
               - Conversations not directly related to CS, such as physics, chemistry, biological science, earth science, engineering, natural disasters, and space, are classified as case 1.
               - If the focus is on mathematics itself rather than CS, it is classified as case 1.
               - The combination of mathematics and computer science is classified as case 2.
               - If the focus is on the relevant engineering field rather than CS, it is classified as case 1.
               - If other engineering is combined with CS, it is classified as case 2.
               - In case 1, you must not provide any recommendation links.
               - In case 2, you must provide links directly related to computer science or development.
               
               3. Summary Order and Format:
               - step 1: Names and numbers of Participants: Check the names of participants to accurately determine the number of people.
               - step 2: Overall Summary: Provide a comprehensive summary of the entire conversation.
               - step 3: Individual Summaries: Summarize what each conversation participant said
               - step 4: Case classification: Classify as case 1 or case 2 based on the comprehensive summary. Here is the most relevant sentence in the context:
                   - If CS is not a core topic in the comprehensive summary, classify it as case 1.
                   - If CS is a core topic in the comprehensive summary, classify it as case 2.
               - step 5: Reasons for classifying cases: Provide reasoning for classification.
                   - In case 1, The reason for classification as case 1 must be explained
                   - In case 2, The reason for classification as case 2 must be explained
               - step 6: Whether to provide referral links: Separated into case1 and case2.
                   - In case 1, you must not provide any recommendation links.
                   - In case 2, you must provide links directly related to computer science or development. Recommend two official sites.
               
               [case 1 example]
               참여자: 영준, 슬기, 민수, 지현, 수민, 하은(6명)
               전체 요약: 지진에 대한 경험과 대비 방법에 대해 이야기했습니다. 안전한 대피와 비상용품 준비의 중요성을 강조했습니다.
               개별 요약:
               영준: 우리나라 건물들의 지진 대비가 부족하다는 이야기를 나눴습니다.
               슬기: 일본의 지진 대비와 비교하여 우리나라의 상황을 언급했습니다.
               민수: 지진 대피 훈련과 가족과의 대피 계획 수립의 중요성을 강조했습니다.
               지현: 스마트폰 앱을 활용한 지진 경보 시스템에 대해 이야기했습니다.
               수민: 지진 대비 앱을 설치한 경험을 공유했습니다.
               하은: 지진 대비에 대한 정보 공유와 서로 준비하는 중요성을 강조했습니다.
               케이스 1로 분류한 이유: 전반적으로 지진 대비와 관련된 대화였습니다.
               
               ////////////////////////////////////////////////////////////////////////////////
               
               [case 2 example]
               참여자: 지현, 민준, 수연, 지후, 하나(5명)
               전체 요약: 인공지능과 관련하여 자연어 처리와 윤리, 블록체인 기술의 확장성 문제, 양자 컴퓨팅의 암호화 분야 적용 등에 대해 토의했습니다.
               개별 요약:
               지현: 자연어 처리와 양자 컴퓨팅의 향후 가능성을 강조했습니다.
               민준: GPT-3 경험과 양자 컴퓨팅이 암호화 분야에 미치는 영향에 주목했습니다.
               수연: 인공지능 윤리와 블록체인의 확장성 문제에 대한 관심을 표현했습니다.
               지후: 인공지능 윤리와 블록체인의 확장성 문제 해결을 위한 방법에 대해 논의했습니다.
               하나: 페어 AI와 블록체인에 대한 연구 및 양자 컴퓨팅의 중요성에 관심을 나타냈습니다.
               케이스 2로 분류한 이유: 인공지능과 관련된 여러 주제에 대해 토의하였습니다.
               추천 링크:
               https://www.aitimes.com/
               https://www.ibm.com/kr-ko
               
               4. Let's Think Step by Step:
               - step1: Initial Response Generation: Generate the initial response based on the chat content.
               - step2: Check for Relevance:
                   - Verify if the summary is related to computer science or development.
                   - If not, classify as case 1 and proceed with a non-CS related summary without site recommendations.
                   - If yes, classify as case 2 and proceed with site recommendations.
               - step3: Summary Length Check: Ensure the summary is neither too short nor exceeds 200 characters.
               - step4: Language Verification: Confirm the summary is written in formal Korean.
               - step5: Individual and Overall Summary Check: Ensure individual and overall summaries are provided and accurate.
               - step6: Site Recommendation Check:
                   - If classified as case 1, do not create referral links.
                   - If classified as case 2, ensure two relevant links are provided.
                   - Verify the links are directly related to the chat topic and are functional (no 404 pages).
                   - Ensure two official sites are recommended.
               - step7: Final Validation: Review the entire response to ensure all guidelines and steps are properly followed.
               - step8: Repeat if Necessary: If any step fails, regenerate the response and repeat the validation process up to a maximum of 10 attempts.
               
               5. Hallucination Prevention Guidelines:
               - Clearly state when uncertain or lacking sufficient information.
               - Base responses on the knowledge you have been trained on, which is up to September 2021. For information beyond September 2021, recommend reliable embedded sites.
               - Do not fabricate facts, names, or specific details not provided in your training data.
               - Encourage users to verify critical or sensitive information from reliable external sources.
               - Avoid providing incorrect URL formats or links that may lead to 404 pages (not found).
               - Use Chain of Thought and Step-by-Step Thinking principles:
                   - Split complex tasks into simpler subtasks (Divide et Impera).
                   - Give time to think before concluding.
                   - Guide to find solutions independently.
                   - Check if anything was missed in previous attempts.
               """;
        ChatGPTRequest request = new ChatGPTRequest(model, systemPrompt, userPrompt.getMessage());
        ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, request,
            ChatGPTResponse.class);
        String summary = chatGPTResponse.getChoices().get(0).getMessage().getContent();
        List<String> recommendedSites = embeddingService.recommendSites(summary);

        return ApiResponse.onSuccess(ChatgptApiResponse.SendMessageResultDTO.builder()
            .message(chatGPTResponse.getChoices().get(0).getMessage().getContent())
            .recommemdedSites(recommendedSites)
            .build()
        );
    }

}
