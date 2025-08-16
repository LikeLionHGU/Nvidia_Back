package com.likelionhgu.nvidia.controller.response;

import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.dto.EnrollmentDto;
import com.likelionhgu.nvidia.dto.EnrollmentTimeDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class EnrollmentListResponse {
    private String enPhoneNumber;
    private Address address;
    private String account;
    private int maxPeople;
    private int price;
    private String enrolledDate;
    private String enrolledTime;

    public static EnrollmentListResponse from(EnrollmentDto enrollmentDto) {
        return EnrollmentListResponse.builder()
                .enPhoneNumber(enrollmentDto.getPhoneNumber())
                .address(enrollmentDto.getAddress())
                .account(enrollmentDto.getAccount())
                .maxPeople(enrollmentDto.getMaxPeople())
                .price(enrollmentDto.getPrice())
                .enrolledDate(enrollmentDto.getEnrolledDate())
                .enrolledTime(enrollmentDto.getEnrolledTime())
                .build();
    }
}
