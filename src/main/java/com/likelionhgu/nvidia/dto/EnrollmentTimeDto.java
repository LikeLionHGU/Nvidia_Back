package com.likelionhgu.nvidia.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Getter @Setter
public class EnrollmentTimeDto {
    private LocalDate date;
    private Set<Integer> selectedTimeSlotIndex = new TreeSet<>();
}

//TODO: List에서 Set으로 바꾸었고 Slot 객체를 없애고 바로 인덱스 넣도록 Integer를 요소로 설정했다 (API 명세서 수정 필요)
