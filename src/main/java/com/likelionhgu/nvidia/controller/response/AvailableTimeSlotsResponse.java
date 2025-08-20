package com.likelionhgu.nvidia.controller.response;

import com.likelionhgu.nvidia.dto.AvailableTimeSlotsDto;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class AvailableTimeSlotsResponse {
    private Set<Integer> timeSlots;

    public static AvailableTimeSlotsResponse from(AvailableTimeSlotsDto dto){
        return AvailableTimeSlotsResponse.builder()
                .timeSlots(dto.getTimeSlots())
                .build();
    }
}
