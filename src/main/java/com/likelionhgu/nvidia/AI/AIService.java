//package com.likelionhgu.nvidia.AI;
//
//import lombok.RequiredArgsConstructor;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.HttpStatusCodeException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//import java.util.Objects;
//
//@Service
//@RequiredArgsConstructor
//public class AIService {
//
//    @Value("${gemini.api.key}")
//    private String geminiKey;
//
//    public String callGemini(String userMessage) throws JSONException {
//        String apiKey = Objects.requireNonNull(geminiKey, "gemini.api.key is null/empty");
//        if (apiKey.isBlank()) throw new IllegalStateException("gemini.api.key is blank");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("x-goog-api-key", apiKey); // ✅ 정식 방법
//
//        JSONObject body = new JSONObject()
//                .put("contents", List.of(new JSONObject()
//                        .put("parts", List.of(new JSONObject().put("text", userMessage)))));
//
//        String url = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent"; // 테스트는 flash 권장
//        RestTemplate rt = new RestTemplate();
//
//        try {
//            return rt.postForEntity(url, new HttpEntity<>(body.toString(), headers), String.class).getBody();
//        } catch (HttpStatusCodeException e) {
//            // 구글 응답 그대로 확인 가능
//            throw new RuntimeException(e.getResponseBodyAsString(), e);
//        }
//    }
//}
