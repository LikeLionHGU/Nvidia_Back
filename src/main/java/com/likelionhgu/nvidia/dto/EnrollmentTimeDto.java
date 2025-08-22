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