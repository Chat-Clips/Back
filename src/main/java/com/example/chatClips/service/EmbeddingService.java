package com.example.chatClips.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
//import okhttp3.MediaType; //위와 겹침으로 임베딩 추출시 사용
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmbeddingService {

    @Value("${openai.api.key}")
    private String openAiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/engines/text-embedding-ada-002/embeddings";

    public List<String> recommendSites(String summary) {
        // recommen.txt 파일에서 사이트와 임베딩 벡터 불러오기
        List<String> sites = new ArrayList<>();
        List<double[]> embeddings = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/recommen.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                // 사이트와 임베딩 분리
                int startIndex = line.indexOf('[');
                String site = line.substring(0, startIndex).trim();
                String embeddingStr = line.substring(startIndex + 1, line.length() - 1).trim();

                // 사이트 리스트에 추가
                sites.add(site);

                // 임베딩 문자열을 double 배열로 변환
                String[] embeddingParts = embeddingStr.split(",");
                double[] embedding = new double[embeddingParts.length];
                for (int i = 0; i < embeddingParts.length; i++) {
                    embedding[i] = Double.parseDouble(embeddingParts[i].trim());
                }

                // 임베딩 리스트에 추가
                embeddings.add(embedding);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 채팅 요약본의 임베딩 벡터 생성
        double[] summaryEmbedding = generateEmbedding(summary);

        // 유사도 계산
        double maxSimilarity = -1;
        String recommendedSite = null;
        double threshold =0.034;
        for (int i = 0; i < embeddings.size(); i++) {
            double[] embedding = embeddings.get(i);
            double similarity = calculateSimilarity(summaryEmbedding, embedding);
            if (similarity > maxSimilarity && similarity > threshold) {
                maxSimilarity = similarity;
                recommendedSite = sites.get(i);
            }
        }

        List<String> recommendedSites = new ArrayList<>();
        if (recommendedSite != null) {
            recommendedSites.add(recommendedSite);
        }
        if (recommendedSite == null) {
            recommendedSites.add("-");
        }

        return recommendedSites;
    }


    private double[] generateEmbedding(String text) {
        // RestTemplate 초기화
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정 (API 키 추가)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ openAiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 본문 설정 (텍스트 입력)
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("input", text);

        // HTTP 요청 엔티티 생성
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // OpenAI API 엔드포인트에 POST 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(OPENAI_API_URL, requestEntity, String.class);

        // 응답 본문 가져오기
        String responseBody = responseEntity.getBody();

        // 응답 본문 파싱하여 임베딩 추출
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode dataArray = rootNode.get("data");

            // 첫 번째 데이터 항목의 임베딩 추출
            if (dataArray.isArray() && dataArray.size() > 0) {
                JsonNode embeddingNode = dataArray.get(0).get("embedding");

                // 임베딩 배열로 변환
                double[] embedding;
                if (embeddingNode.isArray()) {
                    embedding = new double[embeddingNode.size()];
                    for (int i = 0; i < embeddingNode.size(); i++) {
                        embedding[i] = embeddingNode.get(i).asDouble();
                    }
                } else {
                    throw new IllegalArgumentException("Embedding is not an array");
                }

                return embedding;
            } else {
                throw new IllegalArgumentException("No data found in response");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private double calculateSimilarity(double[] vector1, double[] vector2) {
        // 두 벡터 간의 유사도 계산
        // 코사인 유사도를 계산하는 예시입니다.
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            norm1 += Math.pow(vector1[i], 2);
            norm2 += Math.pow(vector2[i], 2);
        }
        norm1 = Math.sqrt(norm1);
        norm2 = Math.sqrt(norm2);
        if (norm1 == 0 || norm2 == 0) {
            return 0.0; // 분모가 0인 경우 유사도를 0으로 반환합니다.
        }
        return dotProduct / (norm1 * norm2);
    }

    //<임베딩 벡터 추출>
 /*   private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new Gson();
    public String embedTextFromFile(String filePath) throws IOException {
        // 파일 읽기
        // ClassPathResource를 사용하여 InputStream 얻기
        ClassPathResource resource = new ClassPathResource(filePath);
        String content;
        try (InputStream inputStream = resource.getInputStream()) {
            content = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        }


        // GPT API 요청 바디 구성
        String jsonBody = gson.toJson(new EmbedRequest(content));
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));

        // GPT API 호출
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/engines/text-embedding-ada-002/embeddings")
                .addHeader("Authorization", "Bearer " + openAiKey)
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // 응답 바디 반환
            return response.body().string();
        }
    }

    static class EmbedRequest {
        String input;

        public EmbedRequest(String input) {
            this.input = input;
        }
    }*/
}
