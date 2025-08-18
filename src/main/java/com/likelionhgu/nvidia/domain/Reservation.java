package com.likelionhgu.nvidia.domain;

import com.likelionhgu.nvidia.controller.request.ReservationRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

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
    private LocalDate date;
    private Set<Integer> slotIndex = new TreeSet<>();

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    public static Reservation from(ReservationRequest reservationRequest){
        return Reservation.builder()
                .reName(reservationRequest.getReName())
                .reName(reservationRequest.getReName())
                .rePhoneNumber(reservationRequest.getRePhoneNumber())
                .date(reservationRequest.getDate())
                .slotIndex(reservationRequest.getSelectedTimeSlot())
                .build();
    }

    public static Reservation from(Room room, ReservationRequest reservationRequest){
        return Reservation.builder()
                .reName(reservationRequest.getReName())
                .reName(reservationRequest.getReName())
                .rePhoneNumber(reservationRequest.getRePhoneNumber())
                .date(reservationRequest.getDate())
                .slotIndex(reservationRequest.getSelectedTimeSlot())
                .room(room)
                .build();
    }
}
