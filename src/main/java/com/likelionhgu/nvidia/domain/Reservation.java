package com.likelionhgu.nvidia.domain;

import com.likelionhgu.nvidia.controller.request.ReservationRequest;
import com.likelionhgu.nvidia.dto.ReservationTimeDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reName;
    private String rePhoneNumber;
    private ReservationTimeDto selectedTime;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    public static Reservation from(ReservationRequest reservationRequest){
        return Reservation.builder()
                .reName(reservationRequest.getReName())
                .reName(reservationRequest.getReName())
                .rePhoneNumber(reservationRequest.getRePhoneNumber())
                .selectedTime(reservationRequest.getReservationTime())
                .build();
    }

    public static Reservation from(Room room, ReservationRequest reservationRequest){
        return Reservation.builder()
                .reName(reservationRequest.getReName())
                .reName(reservationRequest.getReName())
                .rePhoneNumber(reservationRequest.getRePhoneNumber())
                .selectedTime(reservationRequest.getReservationTime())
                .room(room)
                .build();
    }
}
