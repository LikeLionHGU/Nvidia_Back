package com.likelionhgu.nvidia.dto;

import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter @Setter
@AllArgsConstructor
@Builder
public class ReservationDto {
    private Long roomId;
    private List<String> photo;
    private Address address;
    private String phoneNumber;
    private String account;
    private int maxPeople;
    private int totalPrice; // 단위 가격 * 시간 (총 가격)
    private int selectedHour;
    private String reservedDate;
    //TODO: String이 아닌 시간 슬롯을 보내주는 게 맞는 것 같아 수정했는데 확인 필요
    private Set<Integer> reservedTime;

    public static ReservationDto from(Reservation reservation){
        return ReservationDto.builder()
                .roomId(reservation.getRoom().getId())
                .photo(reservation.getRoom().getPhotoList())
                .address(reservation.getRoom().getAddress())
                .phoneNumber(reservation.getRePhoneNumber())
                .account(reservation.getRoom().getAccount())
                .maxPeople(reservation.getRoom().getMaxPeople())
                .selectedHour(reservation.getSlotIndex().size())
                .totalPrice(reservation.getRoom().getPrice() * reservation.getSlotIndex().size())
                .reservedDate(reservation.getDate())
                .reservedTime(reservation.getSlotIndex())
                .build();

    }
}
