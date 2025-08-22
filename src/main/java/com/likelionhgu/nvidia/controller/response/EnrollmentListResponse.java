package com.likelionhgu.nvidia.controller.response;

import com.likelionhgu.nvidia.dto.AddressDto;
import com.likelionhgu.nvidia.dto.EnrollmentDto;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class EnrollmentListResponse {
    private Long roomId;
    private String photo;
    private AddressDto address;
    private String phoneNumber;
    private String account;
    private int maxPeople;
    private int price;
    private LocalDate enrolledDate;
    private Set<Integer> enrolledTime  = new TreeSet<>();

    public static EnrollmentListResponse from(EnrollmentDto enrollmentDto) {
        return EnrollmentListResponse.builder()
                .roomId(enrollmentDto.getRoomId())
                .photo(enrollmentDto.getPhoto())
                .address(enrollmentDto.getAddress())
                .phoneNumber(enrollmentDto.getPhoneNumber())
                .account(enrollmentDto.getAccount())
                .maxPeople(enrollmentDto.getMaxPeople())
                .price(enrollmentDto.getPrice())
                .enrolledDate(enrollmentDto.getEnrolledDate())
                .enrolledTime(enrollmentDto.getEnrolledTime())
                .build();
    }
}
