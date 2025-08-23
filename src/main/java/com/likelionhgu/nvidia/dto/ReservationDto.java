package com.likelionhgu.nvidia.dto;

import com.likelionhgu.nvidia.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter @Setter
@AllArgsConstructor
@Builder
public class ReservationDto {
    private Long roomId;
    private List<String> photo;
    private AddressDto address;
    private String phoneNumber;
    private String account;
    private int maxPeople;
    private int totalPrice; // 단위 가격 * 시간 (총 가격)
    private int selectedHour;
    private LocalDate reservedDate;
    private Set<Integer> reservedTime;

    public static ReservationDto from(Reservation reservation){
        return ReservationDto.builder()
                .roomId(reservation.getRoom().getId())
                .photo(reservation.getRoom().getPhotoList())
                .address(AddressDto.from(reservation.getRoom().getAddress()))
                .phoneNumber(reservation.getPhoneNumber())
                .account(reservation.getRoom().getAccount())
                .maxPeople(reservation.getRoom().getMaxPeople())
                .selectedHour(reservation.getSlotIndex().size())
                .totalPrice(reservation.getRoom().getPrice() * reservation.getSlotIndex().size())
                .reservedDate(reservation.getDate())
                .reservedTime(reservation.getSlotIndex())
                .build();

    }
}
