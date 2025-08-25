package com.likelionhgu.nvidia.dto;

import com.likelionhgu.nvidia.domain.Schedule;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ScheduleDto {
    private LocalDate date;
    private Set<Integer> slotIndex = new TreeSet<>();
    private String enPhoneNumber;
    private String rePhoneNumber;

    public static ScheduleDto from(Schedule schedule) {
        return ScheduleDto.builder()
                .date(schedule.getDate())
                .slotIndex(schedule.getSlotIndex())
                .enPhoneNumber(schedule.getEnPhoneNumber())
                .rePhoneNumber(schedule.getRePhoneNumber())
                .build();
    }
}
