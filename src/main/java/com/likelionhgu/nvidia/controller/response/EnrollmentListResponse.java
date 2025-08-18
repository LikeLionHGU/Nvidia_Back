package com.likelionhgu.nvidia.controller.response;

import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.dto.EnrollmentDto;
import com.likelionhgu.nvidia.dto.EnrollmentTimeDto;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
    //TODO: LocalDate로 안 해도 되는지 확인. String이 필요한지 파악 필요.
    private String enrolledDate;
    //TODO: enrolledTime String -> Set<Integer>로 변경. 문제 없는지 확인 필요 (API 명세)
    private Set<Integer> enrolledTime  = new TreeSet<>();

    public static EnrollmentListResponse from(EnrollmentDto enrollmentDto) {
        return EnrollmentListResponse.builder()
                .enPhoneNumber(enrollmentDto.getPhoneNumber())
                .address(enrollmentDto.getAddress())
                .account(enrollmentDto.getAccount())
                .maxPeople(enrollmentDto.getMaxPeople())
                .price(enrollmentDto.getPrice())
                .enrolledDate(enrollmentDto.getEnrolledDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .enrolledTime(enrollmentDto.getEnrolledTime())
                .build();
    }
}
