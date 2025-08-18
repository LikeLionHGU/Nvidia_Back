package com.likelionhgu.nvidia.controller.response;

import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.domain.Reservation;
import com.likelionhgu.nvidia.domain.Schedule;
import com.likelionhgu.nvidia.dto.ReservationDto;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ReservationListResponse {
    private Long roomId;
    private List<String> photo;
    private Address address;
    private String phoneNumber;
    private String account;
    private int maxPeople;
    private int totalPrice; // 단위 가격 * 시간 (총 가격)
    private int selectedHour;
    private String reservedDate;
    private Set<Integer> reservedTime;

    public static ReservationListResponse from(ReservationDto reservationDto){
        return ReservationListResponse.builder()
                .roomId(reservationDto.getRoomId())
                .photo(reservationDto.getPhoto())
                .address(reservationDto.getAddress())
                .phoneNumber(reservationDto.getPhoneNumber())
                .account(reservationDto.getAccount())
                .maxPeople(reservationDto.getMaxPeople())
                .selectedHour(reservationDto.getSelectedHour())
                .totalPrice(reservationDto.getTotalPrice())
                //TODO: LocalDate -> String 변환시킴. String이어야 하는 이유가 있는지 확인 필요
                .reservedDate(reservationDto.getReservedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .reservedTime(reservationDto.getReservedTime())
                .build();
    }
}