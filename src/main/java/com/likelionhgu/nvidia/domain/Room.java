package com.likelionhgu.nvidia.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "room")
public class Room {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String enName;
    private String enPhoneNumber;

    // 숫자 ID 대신 연관관계로 변경
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    private String account;
    private int maxPeople;
    private int price;

    @Column(length = 2000)
    private String memo;

    private List<String> optionList = new ArrayList<>();
    private List<String> chipList = new ArrayList<>();
    private List<String> photoList = new ArrayList<>();

    @OneToMany(
            mappedBy = "room",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<Reservation> reservations = new ArrayList<>();
}
