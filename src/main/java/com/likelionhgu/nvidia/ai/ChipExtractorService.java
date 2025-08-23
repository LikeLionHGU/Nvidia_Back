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
        - 입력된 텍스트와 연관된 모든 chip을 반환합니다.
        - chip 이름은 canonical_name만 사용합니다.
        - 새로운 chip을 만들지 않습니다.
        - 동의어/오탈자는 가장 가까운 canonical chip으로 매핑합니다.
        - 부정형(예: "~아닌", "not ~") 표현은 해당 chip을 선택하지 않습니다.
        - 반드시 아래 JSON 스키마에 맞춰 응답합니다.
        허용 chip(canonical):
        활기찬, 따뜻한, 포근한, 여유로운, 레트로, 영감을 주는, 모던한, 로맨틱, 세련된,
        컬러풀한, 깔끔한, 심플, 감성적인, 회의, 스터디, 촬영, 상담, 연습, 시험, 조용한,
        넓은, 이벤트, 럭셔리, 유니크, 창의적인, 빈티지
        """;

    // schema enum (모델 스키마 강제)
    private static final List<String> ENUM = List.of(
            "활기찬", "따뜻한", "포근한", "여유로운", "레트로", "영감을 주는",
            "모던한", "로맨틱", "세련된", "컬러풀한", "깔끔한", "심플", "감성적인", "회의", "스터디", "촬영",
            "상담", "연습", "시험", "조용한", "넓은", "이벤트", "럭셔리", "유니크", "창의적인", "빈티지"
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

        // 모델 JSON 파싱
        try {
            JsonNode node = om.readTree(json);
            JsonNode chipsNode = node.get("chips");
            String reason = node.has("reason") && node.get("reason").isTextual() ? node.get("reason").asText() : null;

            List<Chip> extractedChips = new ArrayList<>();
            if (chipsNode != null && chipsNode.isArray()) {
                for (JsonNode c : chipsNode) {
                    // 모델이 이미 ENUM 내에서 값을 반환하므로, 추가 변환 없이 바로 Chip으로 변환
                    extractedChips.add(Chip.valueOf(c.asText()));
                }
            }
            return new ChipsResponse(List.copyOf(extractedChips), reason, null);
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

    private static String normalizeToEnum(String s) {
        if (s == null) return null;
        String n = s.trim().replace('-', ' ').replace('_', ' ').replaceAll("\\s+", " ");
        // 특수 매핑
        return (n.equals("영감을 주는") ? "영감을_주는" : s);
    }
}
