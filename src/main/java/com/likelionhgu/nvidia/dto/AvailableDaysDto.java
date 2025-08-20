package com.likelionhgu.nvidia.dto;

import com.likelionhgu.nvidia.domain.Schedule;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class AvailableDaysDto {
    private Set<Integer> days;

    public static AvailableDaysDto from(List<Schedule> schedules) {
        return AvailableDaysDto.builder()
                .days(schedules.stream()
                        // LocalDate에서 일자만 가져오는 로직 포함됨
                        .map(schedule -> schedule.getDate().getDayOfMonth())
                        .collect(Collectors.toSet()))
                .build();
    }
}
