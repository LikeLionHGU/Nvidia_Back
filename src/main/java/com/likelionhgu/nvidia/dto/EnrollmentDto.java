package com.likelionhgu.nvidia.dto;

import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.domain.Room;
import com.likelionhgu.nvidia.domain.Schedule;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Getter @Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class EnrollmentDto {
    private Long roomId;
    private String photo;
    private AddressDto address;
    private String phoneNumber;
    private String account;
    private int maxPeople;
    private int price;
    //TODO: LocalDate로 안 해도 되는지 확인 필요 (String 임의 교체함, API 명세서 수정 필요)
    private LocalDate enrolledDate;
    private Set<Integer> enrolledTime  = new TreeSet<>();

    public static EnrollmentDto from(Room room, Schedule schedule) {
        return EnrollmentDto.builder()
                .roomId(room.getId())
                .photo(room.getPhotoList().get(0))
                .address(AddressDto.from(room.getAddress()))
                .phoneNumber(room.getEnPhoneNumber())
                .account(room.getAccount())
                .maxPeople(room.getMaxPeople())
                .price(room.getPrice())
                //TODO: 이 부분에서 Schedule -> enrolledDate,enrolledTime 데이터 이동 되도록 수정 필요 (아래 맞는지 확인)
                .enrolledDate(schedule.getDate())
                .enrolledTime(schedule.getSlotIndex())
                .build();
    }
}