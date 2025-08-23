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
    private LocalDate enrolledDate;
    private Set<Integer> enrolledTime  = new TreeSet<>();

    private String guestName;
    private String guestPhoneNum;

    public static EnrollmentDto from(Room room, LocalDate date, Set<Integer> slots, String guestName, String guestPhoneNum) {
        return EnrollmentDto.builder()
                .roomId(room.getId())
                .photo(room.getPhotoList().isEmpty() ? null : room.getPhotoList().get(0))
                .address(AddressDto.from(room.getAddress()))
                .phoneNumber(room.getEnPhoneNumber())
                .account(room.getAccount())
                .maxPeople(room.getMaxPeople())
                .price(room.getPrice())
                .enrolledDate(date)
                .enrolledTime(new TreeSet<>(slots))
                .guestName(guestName)
                .guestPhoneNum(guestPhoneNum)
                .build();
    }
}