package com.likelionhgu.nvidia.controller;

import com.likelionhgu.nvidia.domain.Chip;
import com.likelionhgu.nvidia.service.ChipExtractorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final ChipExtractorService chipExtractorService;

    // 요청 DTO
    public record QueryPrompt(@NotBlank String query) {}

    // 응답 DTO (지금은 사용 안 하지만, 나중에 서비스가 DTO를 반환하도록 바꿀 때 쓰세요)
    public record ChipsResponse(List<Chip> chips, String reason, String debug) {
        public static ChipsResponse empty() { return new ChipsResponse(List.of(), null, null); }
    }

    @PostMapping(value = "/gemini/query", produces = "application/json")
    public ResponseEntity<ChipsResponse> query(@RequestBody @Valid QueryPrompt queryPrompt) {
        ChipsResponse res = chipExtractorService.extractChips(queryPrompt.query());
        return ResponseEntity.ok(res);
    }
}
