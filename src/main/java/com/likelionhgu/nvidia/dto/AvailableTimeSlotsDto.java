package com.likelionhgu.nvidia.dto;

import com.likelionhgu.nvidia.domain.Schedule;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class AvailableTimeSlotsDto {
    private Set<Integer> timeSlots;

    public static AvailableTimeSlotsDto from(Schedule schedule){
        return AvailableTimeSlotsDto.builder()
                .timeSlots(schedule.getSlotIndex())
                .build();
    }
}
