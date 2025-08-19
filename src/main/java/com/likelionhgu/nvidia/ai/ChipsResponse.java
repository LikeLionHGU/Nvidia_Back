package com.likelionhgu.nvidia.ai;

import com.likelionhgu.nvidia.domain.Chip;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

/**
 * Gemini 응답을 기반으로 한 Chip 추출 결과 DTO
 */
@Getter
@ToString
@AllArgsConstructor
public class ChipsResponse {

    /** 최종 필터링된 chip 리스트 */
    private final List<Chip> chips;

    /** 모델이 반환한 부가 reason (없을 수 있음) */
    private final String reason;

    /** 에러/차단 사유 유형: "safety", "shape", "parse" 등, 정상일 때는 null */
    private final String errorType;

    /**
     * 정상 결과 여부 (chips가 비어 있어도 errorType이 null이면 정상으로 간주)
     */
    public boolean isOk() {
        return errorType == null;
    }

    /**
     * chips, reason, errorType 전부 빈 상태
     */
    public static ChipsResponse empty() {
        return new ChipsResponse(Collections.emptyList(), null, null);
    }
}
