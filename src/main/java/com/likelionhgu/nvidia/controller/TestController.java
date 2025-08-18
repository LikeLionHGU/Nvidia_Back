package com.likelionhgu.nvidia.controller;

//import com.likelionhgu.nvidia.service.FunctionService;
import com.likelionhgu.nvidia.service.ChipExtractorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final ChipExtractorService chipExtractorService;

    @GetMapping("/gemini/query")
    public ResponseEntity<?> query(@RequestBody QueryPrompt queryPrompt){
        String res = chipExtractorService.extractChips(queryPrompt.getQuery());
        return ResponseEntity.ok().body(res);
    }
}
