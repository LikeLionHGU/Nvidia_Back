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
    private String name;
    private String phoneNumber;
    private LocalDate date;
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<Integer> slotIndex = new TreeSet<>();

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    public static Reservation from(ReservationRequest reservationRequest){
        return Reservation.builder()
                .name(reservationRequest.getName())
                .phoneNumber(reservationRequest.getPhoneNumber())
                .date(LocalDate.parse(reservationRequest.getDate()))
                .slotIndex(reservationRequest.getSchedule())
                .build();
    }

    public static Reservation from(Room room, ReservationRequest reservationRequest){
        return Reservation.builder()
                .name(reservationRequest.getName())
                .phoneNumber(reservationRequest.getPhoneNumber())
                .date(LocalDate.parse(reservationRequest.getDate()))
                .slotIndex(reservationRequest.getSchedule())
                .room(room)
                .build();
    }
}
