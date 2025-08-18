package com.likelionhgu.nvidia.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class ChipExtractorService {

    private final WebClient webClient;

    public ChipExtractorService(@Value("${gemini.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta")
                .defaultHeader("x-goog-api-key", apiKey)
                .build();
    }

    // query 문자열만 받아 JSON 문자열로 반환
    public String extractChips(String query) {
        Map<String, Object> body = Map.of(
                "model", "gemini-2.5-pro",
                "contents", List.of(Map.of(
                        "role", "user",
                        "parts", List.of(Map.of("text", query))
                )),
                "generationConfig", Map.of(
                        "response_mime_type", "application/json"
                )
        );

        Map<?, ?> resp = webClient.post()
                .uri("/models/gemini-2.5-pro:generateContent")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        // JSON은 candidates[0].content.parts[0].text 에 들어옵니다.
        var candidates = (List<Map<String, Object>>) resp.get("candidates");
        if (candidates == null || candidates.isEmpty()) return "{\"chips\":[]}";
        var content = (Map<String, Object>) candidates.get(0).get("content");
        var parts = (List<Map<String, Object>>) content.get("parts");
        if (parts == null || parts.isEmpty()) return "{\"chips\":[]}";
        String json = (String) parts.get(0).get("text");
        return json != null ? json : "{\"chips\":[]}";
    }
}
