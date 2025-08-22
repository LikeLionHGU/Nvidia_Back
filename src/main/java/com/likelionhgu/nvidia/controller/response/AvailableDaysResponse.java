package com.likelionhgu.nvidia.controller.response;

import com.likelionhgu.nvidia.dto.AvailableDaysDto;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class AvailableDaysResponse {
    private Set<Integer> availableDay;

    public static AvailableDaysResponse from(AvailableDaysDto dto){
        return AvailableDaysResponse.builder()
                .availableDay(dto.getDays())
                .build();
    }
}
