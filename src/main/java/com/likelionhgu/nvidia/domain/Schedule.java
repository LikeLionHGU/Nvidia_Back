package com.likelionhgu.nvidia.domain;

import com.likelionhgu.nvidia.controller.request.ReservationRequest;
import com.likelionhgu.nvidia.converter.IntegerSetConverter;
import com.likelionhgu.nvidia.dto.EnrollmentTimeDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Schedule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
//    @Convert(converter = IntegerSetConverter.class)
    private Set<Integer> slotIndex = new TreeSet<>();
    private String enPhoneNumber;
    private String rePhoneNumber;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public static Schedule make(EnrollmentTimeDto enrollmentTimeDto, Room room) {
        return Schedule.builder()
                .date(enrollmentTimeDto.getDate())
                .slotIndex(enrollmentTimeDto.getSelectedTimeSlotIndex())
                .enPhoneNumber(room.getEnPhoneNumber())
                .room(room)
                .build();
    }
}
