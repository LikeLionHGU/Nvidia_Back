// src/main/java/com/likelionhgu/nvidia/service/ChipExtractorService.java
package com.likelionhgu.nvidia.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelionhgu.nvidia.domain.Chip;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChipExtractorService {

    private final WebClient geminiClient;
    private final ObjectMapper om = new ObjectMapper();

    // 프롬프트 룰
    private static final String RULES = """
        규칙:
        - 허용된 chip 목록에서만 선택합니다. 새로운 chip을 만들지 않습니다.
        - chip 이름은 canonical_name만 사용합니다.
        - 동의어/오탈자는 가장 가까운 canonical chip으로 매핑합니다.
        - 부정형(예: "~아닌", "not ~") 표현은 해당 chip을 선택하지 않습니다.
        - 모호하면 빈 배열을 반환합니다.
        - 반드시 아래 JSON 스키마에 맞춰 응답합니다.
        허용 chip(canonical):
        편안한, 아늑한, 조용한, 활기찬, 세련된, 트렌디한, 클래식한, 고급스러운, 따뜻한, 밝은,
        활동적인, 프라이빗한, 개방적인, 협업 친화적, 집중하기 좋은, 창의적인, 모던한, 힙한,
        로맨틱한, 가족 친화적, 저예산, 고급, 교통 편리한, 주차 편리한, 접근성 좋은, 채광 좋은,
        환기 잘되는, 방음 좋은, 장비 완비, 인스타 감성
        """;

    // schema enum (모델 스키마 강제)
    private static final List<String> ENUM = List.of(
            "편안한","아늑한","조용한","활기찬","세련된","트렌디한","클래식한","고급스러운","따뜻한","밝은",
            "활동적인","프라이빗한","개방적인","협업 친화적","집중하기 좋은","창의적인","모던한","힙한",
            "로맨틱한","가족 친화적","저예산","고급","교통 편리한","주차 편리한","접근성 좋은","채광 좋은",
            "환기 잘되는","방음 좋은","장비 완비","인스타 감성"
    );

    public ChipsResponse extractChips(String query) {
        Map<String, Object> body = buildRequestBody(query);

        Map<?, ?> resp;
        try {
            resp = geminiClient.post()
                    .uri("/models/gemini-2.5-pro:generateContent")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception e) {
            // 실패 시 빈 결과
            return ChipsResponse.empty();
        }

        // safety block/피드백 확인
        Map<String, Object> promptFeedback = safeMap(resp.get("promptFeedback"));
        if (!promptFeedback.isEmpty() && promptFeedback.get("blockReason") != null) {
            String reason = "SAFETY_BLOCKED: " + String.valueOf(promptFeedback.get("blockReason"));
            return new ChipsResponse(List.of(), reason, "safety");
        }

        // candidates → content.parts 전수 검사
        List<Map<String, Object>> candidates = safeList(resp.get("candidates"));
        if (candidates.isEmpty()) return new ChipsResponse(List.of(), "NO_CANDIDATE", "shape");

        Map<String, Object> content = safeMap(candidates.get(0).get("content"));
        List<Map<String, Object>> parts = safeList(content.get("parts"));
        if (parts.isEmpty()) return new ChipsResponse(List.of(), "NO_PARTS", "shape");

        String json = findJsonFromParts(parts);
        if (json == null) return new ChipsResponse(List.of(), "NO_TEXT_JSON", "shape");

        // 모델 JSON 파싱 → 서버 enum 필터링
        try {
            JsonNode node = om.readTree(json);
            JsonNode chipsNode = node.get("chips");
            String reason = node.has("reason") && node.get("reason").isTextual() ? node.get("reason").asText() : null;

            List<Chip> filtered = new ArrayList<>();
//            if (chipsNode != null && chipsNode.isArray()) {
//                for (JsonNode c : chipsNode) {
//                    normalizeToEnum(c.asText()).ifPresent(filtered::add);
//                }
//            }
            return new ChipsResponse(List.copyOf(filtered), reason, null);
        } catch (Exception e) {
            return new ChipsResponse(List.of(), "BAD_JSON", "parse");
        }
    }

    // generationConfig에 결정론 세팅 + 스키마 enum 강제
    private Map<String, Object> buildRequestBody(String query) {
        Map<String, Object> responseSchema = Map.of(
                "type", "object",
                "properties", Map.of(
                        "chips", Map.of(
                                "type", "array",
                                "items", Map.of("type", "string", "enum", ENUM)
                        ),
                        "reason", Map.of("type", "string")
                ),
                "required", List.of("chips")
        );

        return Map.of(
                "model", "gemini-2.5-pro",
                "system_instruction", Map.of("parts", List.of(Map.of("text", RULES))),
                "contents", List.of(Map.of("role", "user", "parts", List.of(Map.of("text", query)))),
                "generationConfig", Map.of(
                        "temperature", 0.0,
                        "topK", 1,
                        "topP", 0.0,
                        "candidateCount", 1,
                        "response_mime_type", "application/json",
                        "response_schema", responseSchema
                )
        );
    }

    private static String findJsonFromParts(List<Map<String, Object>> parts) {
        for (Map<String, Object> p : parts) {
            Object t = p.get("text");
            if (t instanceof String s) {
                String trimmed = s.trim();
                if (trimmed.startsWith("{") && trimmed.endsWith("}")) return trimmed;
                if (trimmed.startsWith("```")) {
                    int i = trimmed.indexOf('{');
                    int j = trimmed.lastIndexOf('}');
                    if (i >= 0 && j > i) return trimmed.substring(i, j + 1);
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> safeList(Object o) {
        return (o instanceof List<?> l) ? (List<Map<String, Object>>) l : List.of();
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> safeMap(Object o) {
        return (o instanceof Map<?, ?> m) ? (Map<String, Object>) m : Map.of();
    }

//    private static Optional<Chip> normalizeToEnum(String s) {
//        if (s == null) return Optional.empty();
//        String n = s.trim().replace('-', ' ').replace('_', ' ').replaceAll("\\s+", " ");
//        // 특수 매핑
//        return switch (n) {
//            case "협업 친화적" -> Optional.of(Chip.협업_친화적);
//            case "집중하기 좋은" -> Optional.of(Chip.집중하기_좋은);
//            case "가족 친화적" -> Optional.of(Chip.가족_친화적);
//            case "교통 편리한" -> Optional.of(Chip.교통_편리한);
//            case "주차 편리한" -> Optional.of(Chip.주차_편리한);
//            case "접근성 좋은" -> Optional.of(Chip.접근성_좋은);
//            case "채광 좋은" -> Optional.of(Chip.채광_좋은);
//            case "환기 잘되는" -> Optional.of(Chip.환기_잘되는);
//            case "방음 좋은" -> Optional.of(Chip.방음_좋은);
//            case "장비 완비" -> Optional.of(Chip.장비_완비);
//            case "인스타 감성" -> Optional.of(Chip.인스타_감성);
//            default -> {
//                try {
//                    yield Optional.of(Chip.valueOf(n.replace(' ', '_')));
//                } catch (IllegalArgumentException ex) {
//                    yield Optional.empty();
//                }
//            }
//        };
//    }
}
