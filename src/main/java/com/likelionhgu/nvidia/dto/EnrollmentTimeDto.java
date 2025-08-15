package com.likelionhgu.nvidia.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class EnrollmentTimeDto {
    private String date;
    private List<SlotDto> availableSlot;
}
