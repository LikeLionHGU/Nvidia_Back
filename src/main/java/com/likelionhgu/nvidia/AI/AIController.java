//package com.likelionhgu.nvidia.AI;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.HttpStatusCodeException;
//
//@RestController
//@RequiredArgsConstructor
//public class AIController {
//    private final AIService aiService;
//
//    @PostMapping("/gemini")
//    public ResponseEntity<String> geminicalling(@RequestBody TextRequest req) {
//        try {
//            return ResponseEntity.ok(aiService.callGemini(req.getText()));
//        } catch (HttpStatusCodeException e) {
//            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(e.getMessage());
//        }
//    }
//}
