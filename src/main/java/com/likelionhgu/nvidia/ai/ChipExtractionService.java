package com.likelionhgu.nvidia.ai;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class ChipExtractionService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    // 필요 시 모델명 변경 가능: gemini-2.5-pro, gemini-2.0-flash-exp 등
    private static final String MODEL = "gemini-2.5-pro";

    // 요청에서 사용할 프롬프트 (질문에 주신 문자열 그대로)
    private static final String RULES = """
            규칙:
            1. 입력 텍스트와 연관된 chip들을 모두 선택합니다.
            
            2. chip 이름은 아래 제공하는 canonical 목록의 정확한 표기만 사용합니다(새로운 이름 생성 금지).
            
            3. 동의어/오탈자는 가장 가까운 canonical chip으로 매핑합니다.
            
            4. 부정(예: "아닌", "not ", " 빼고", " 제외")이 붙은 표현은 해당 chip을 선택하지 않습니다.
            
            5. 중복은 제거합니다. 결과 순서는 입력의 중요도/직접성 기준으로 자연스럽게 나열합니다.
            
            6. 아래 JSON 스키마에 ‘정확히’ 맞춰 응답합니다. JSON 외 다른 텍스트, 주석, 코드펜스(```), 설명 금지. 매칭되는 것이 없으면 chips는 빈 배열로 반환합니다.
            
            - 허용 chip(canonical — enum과 동일 표기):
            활기찬, 따뜻한, 포근한, 여유로운, 레트로, 영감을_주는, 모던한, 로맨틱, 세련된, 컬러풀한, 깔끔한, 심플, 감성적인, 회의, 스터디, 촬영, 상담, 연습, 시험, 조용한, 넓은, 이벤트, 럭셔리, 유니크, 창의적인, 빈티지
            
            - 응답 JSON 스키마(반드시 준수):
            {
            "chips": [
            { "chip": "<위 canonical 중 하나>" }
            ]
            }
            
            입력 예시와 정답 예시:
            입력: "모던하고 깔끔한 회의실, 시끄러운 곳은 싫어요"
            정답:
            {
            "chips": [
            { "chip": "모던한" },
            { "chip": "깔끔한" },
            { "chip": "회의" },
            { "chip": "조용한" }
            ]
            }
            
            입력: "레트로 감성 원하지만 럭셔리는 아니야"
            정답:
            {
            "chips": [
            { "chip": "레트로" },
            { "chip": "감성적인" }
            ]
            }
        """;

    public ChipExtractionService(@Value("${gemini.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta")
                .defaultHeader("x-goog-api-key", apiKey)
                .build();

        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 사용자 입력을 넣으면 ChipsJSON으로 반환합니다.
     * 예외/빈 응답 시 안전하게 빈 배열로 처리합니다.
     */
    public ChipsJSON extractChips(String userText) {
        // Gemini v1beta generateContent 요청 바디
        Map<String, Object> body = Map.of(
                "model", MODEL,
                "contents", List.of(Map.of(
                        "role", "user",
                        "parts", List.of(
                                Map.of("text", RULES + "\n\n이제 아래 입력에 대해 JSON만 출력하세요:\n\"" + sanitize(userText) + "\"")
                        )
                )),
                "generationConfig", Map.of(
                        "response_mime_type", "application/json"
                )
        );

        // 호출
        Map<?, ?> resp = webClient.post()
                .uri("/models/" + MODEL + ":generateContent")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        // candidates[0].content.parts[*].text 중 첫 텍스트 사용
        String jsonText = extractFirstText(resp);
        if (jsonText == null || jsonText.isBlank()) {
            return emptyResult();
        }

        // 혹시 모를 코드펜스 제거
        jsonText = stripCodeFences(jsonText.trim());

        try {
            ChipsJSON result = objectMapper.readValue(jsonText, ChipsJSON.class);
            // 방어적 검증
            if (result.getChips() == null) {
                result.setChips(List.of());
            }
            return result;
        } catch (Exception e) {
            // 역직렬화 실패 시 빈 결과
            return emptyResult();
        }
    }

    /**
     * chips -> 문자열 리스트로만 받고 싶을 때.
     */
    public List<String> extractChipStrings(String userText) {
        ChipsJSON r = extractChips(userText);
        return r.getChips().stream().map(ChipJSON::getChip).toList();
    }

    // ---------- 내부 유틸 ----------

    private static ChipsJSON emptyResult() {
        ChipsJSON cj = new ChipsJSON();
        cj.setChips(List.of());
        return cj;
    }

    private static String sanitize(String s) {
        // 큰따옴표 등 최소 이스케이프 (프롬프트 삽입 안전용)
        return s.replace("\"", "\\\"");
    }

    @SuppressWarnings("unchecked")
    private static String extractFirstText(Map<?, ?> resp) {
        if (resp == null) return null;
        Object candidates = resp.get("candidates");
        if (!(candidates instanceof List<?> candList) || candList.isEmpty()) return null;

        Object first = candList.get(0);
        if (!(first instanceof Map<?, ?> firstMap)) return null;

        Object content = firstMap.get("content");
        if (!(content instanceof Map<?, ?> contentMap)) return null;

        Object parts = contentMap.get("parts");
        if (!(parts instanceof List<?> partsList) || partsList.isEmpty()) return null;

        for (Object p : partsList) {
            if (p instanceof Map<?, ?> pm) {
                Object t = pm.get("text");
                if (t instanceof String ts && !ts.isBlank()) {
                    return ts;
                }
            }
        }
        return null;
    }

    private static String stripCodeFences(String s) {
        String trimmed = s.trim();
        // ```json ... ``` or ``` ... ```
        if (trimmed.startsWith("```")) {
            // 첫 줄의 ```json 혹은 ``` 제거
            int firstNewline = trimmed.indexOf('\n');
            if (firstNewline > 0) {
                trimmed = trimmed.substring(firstNewline + 1);
            }
            // 마지막 ``` 제거
            int lastFence = trimmed.lastIndexOf("```");
            if (lastFence >= 0) {
                trimmed = trimmed.substring(0, lastFence);
            }
            return trimmed.trim();
        }
        return s;
    }
}
