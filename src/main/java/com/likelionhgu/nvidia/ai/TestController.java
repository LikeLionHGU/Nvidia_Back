package com.likelionhgu.nvidia.ai;

import com.likelionhgu.nvidia.domain.Chip;
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

    @PostMapping(value = "/gemini/query", produces = "application/json")
    public ResponseEntity<ChipsResponse> query(@RequestBody QueryRequest query) {
        ChipsResponse res = chipExtractorService.extractChips(query.getQuery());
        return ResponseEntity.ok(res);
    }
}
