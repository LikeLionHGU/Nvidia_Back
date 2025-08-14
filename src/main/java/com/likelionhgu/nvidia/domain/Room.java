package com.likelionhgu.nvidia.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String enName;
    private String enPhoneNumber;
    private Long addressId;
    private String account;
    private int maxPeople;
    private int price;
    private String memo;
    private List<String> optionList;
    private List<String> chipList;
    private List<String> photoList;

    @OneToMany(
            mappedBy = "room",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(
            mappedBy = "room",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Schedule> schedules = new ArrayList<>();
}

