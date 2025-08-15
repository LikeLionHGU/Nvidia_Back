package com.likelionhgu.nvidia.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ReAndEnDto {
    private List<ReservationTimeDto>  reservations;
    private List<EnrollmentTimeDto> enrollments;
}
