package com.likelionhgu.nvidia.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Schedule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private int slotIndex;
    private String rePhoneNumber;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}
