package com.likelionhgu.nvidia.dto;

import com.likelionhgu.nvidia.domain.Reservation;
import com.likelionhgu.nvidia.domain.Room;
import lombok.*;

import java.time.LocalDate;
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
    private Set<Integer> enrolledTime = new TreeSet<>();

    private String guestName;
    private String guestPhoneNum;

    public static EnrollmentDto from(Room room, Reservation reservation) {
        return EnrollmentDto.builder()
                .roomId(room.getId())
                .photo(room.getPhotoList().isEmpty() ? null : room.getPhotoList().get(0))
                .address(AddressDto.from(room.getAddress()))
                .phoneNumber(room.getEnPhoneNumber())
                .account(room.getAccount())
                .maxPeople(room.getMaxPeople())
                .price(room.getPrice())
                .enrolledDate(reservation.getDate())
                .enrolledTime(reservation.getSlotIndex())
                .guestName(reservation.getName())
                .guestPhoneNum(reservation.getPhoneNumber())
                .build();
    }
}