package com.likelionhgu.nvidia.dto;

import com.likelionhgu.nvidia.domain.Reservation;
import com.likelionhgu.nvidia.domain.Schedule;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class AvailableTimeSlotsDto {
    private Set<Integer> timeSlots; // 남은 슬롯만 담음

    // 기존: 원본 슬롯 그대로 (필요시 유지)
    public static AvailableTimeSlotsDto from(Schedule schedule){
        return AvailableTimeSlotsDto.builder()
                .timeSlots(schedule == null || schedule.getSlotIndex() == null ? Set.of()
                        : new HashSet<>(schedule.getSlotIndex()))
                .build();
    }

    // 신규: 예약 목록을 반영해 남은 슬롯만 반환
    public static AvailableTimeSlotsDto from(Schedule schedule, List<Reservation> reservations){
        // 스케줄이 없으면 남은 슬롯도 없음
        if (schedule == null || schedule.getSlotIndex() == null) {
            return AvailableTimeSlotsDto.builder().timeSlots(Set.of()).build();
        }

        // 원본 손상 방지: 복사본으로 작업
        Set<Integer> available = new HashSet<>(schedule.getSlotIndex());

        // 날짜 동일한 예약들의 모든 슬롯(합집합)
        Set<Integer> reserved = reservations == null ? Set.of() :
                reservations.stream()
                        .filter(r -> r != null && r.getSlotIndex() != null)
                        .flatMap(r -> r.getSlotIndex().stream())
                        .collect(Collectors.toSet());

        // 예약 슬롯 제외
        available.removeAll(reserved);

        return AvailableTimeSlotsDto.builder()
                .timeSlots(available)
                .build();
    }
}
