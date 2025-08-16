package com.likelionhgu.nvidia.dto;

import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.domain.Room;
import lombok.*;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class EnrollmentDto {
    private Long roomId;
    private Address address;
    private String phoneNumber;
    private String account;
    private int maxPeople;
    private int price;
    //TODO: LocalDate로 안 해도 되는지 확인 필요
    private String enrolledDate;
    private String enrolledTime;

    public static EnrollmentDto from(Room room) {
        return EnrollmentDto.builder()
                .roomId(room.getId())
                .address(room.getAddress())
                .phoneNumber(room.getEnPhoneNumber())
                .account(room.getAccount())
                .maxPeople(room.getMaxPeople())
                .price(room.getPrice())
                //TODO: 이 부분에서 Schedule -> enrolledDate,enrolledTime 데이터 이동 되도록 수정 필요
                .build();
    }
}