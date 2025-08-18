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
        String rules = """
      규칙:
      - 허용된 chip 목록에서만 선택합니다. 새로운 chip을 만들지 않습니다. chip은 중괄호 안에 주어집니다.
      - chip 이름은 반드시 "canonical_name"(백엔드에서 정의한 표준명)만 사용합니다.
      - 동의어/유의어/오탈자/비격식 표현은 가장 가까운 canonical chip으로 매핑합니다.
      - 부정어(예: "~아닌", "덜 ~", "not ~")가 있으면 해당 chip은 선택하지 않습니다.
      - 강도 표현(아주/매우/살짝 등)은 chip 선택 여부에만 사용하고, 점수는 주지 않습니다.
      - 모호하거나 해당 없음이면 빈 배열을 반환합니다.
      - 출력은 반드시 JSON이며, 지정된 스키마 이외의 텍스트를 포함하지 않습니다.
      - chip 목록은 아래와 같습니다.
      {편안한, 아늑한, 조용한, 활기찬, 세련된, 트렌디한, 클래식한, 고급스러운, 따뜻한, 밝은, 활동적인, 프라이빗한 (개인 공간 보장), 개방적인, 협업 친화적 (회의, 워크샵), 집중하기 좋은, 창의적인, 모던한, 힙한, 로맨틱한, 가족 친화적, 저예산 (가성비 좋은), 고급 (프리미엄), 교통 편리한, 주차 편리한, 접근성 좋은, 채광 좋은, 환기 잘되는, 방음 좋은, 장비 완비 (빔, 화이트보드 등), 인스타 감성 (사진 찍기 좋은)}
      """;

        Map<String, Object> body = Map.of(
                "model", "gemini-2.5-pro",
                "contents", List.of(
                        Map.of("role", "user", "parts", List.of(Map.of("text", rules))),
                        Map.of("role", "user", "parts", List.of(Map.of("text", query)))
                ),
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

        var candidates = (List<Map<String, Object>>) resp.get("candidates");
        if (candidates == null || candidates.isEmpty()) return "{\"chips\":[]}";
        var content = (Map<String, Object>) candidates.get(0).get("content");
        var parts = (List<Map<String, Object>>) content.get("parts");
        if (parts == null || parts.isEmpty()) return "{\"chips\":[]}";
        String json = (String) parts.get(0).get("text");
        return json != null ? json : "{\"chips\":[]}";
    }
}
