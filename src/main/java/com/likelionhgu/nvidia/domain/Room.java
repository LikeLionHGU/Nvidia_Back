package com.likelionhgu.nvidia.domain;

import com.likelionhgu.nvidia.controller.request.EnrollmentRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
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

    @ElementCollection(fetch = FetchType.LAZY)
    @OrderColumn
    @Column(length = 2000)
    private List<String> optionList = new ArrayList<>();
    @ElementCollection(fetch = FetchType.LAZY)
    @OrderColumn
    @Column(length = 2000)
    private List<String> chipList = new ArrayList<>();
    @ElementCollection(fetch = FetchType.LAZY)
    @OrderColumn
    @Column(length = 2000)
    private List<String> photoList = new ArrayList<>();

    @OneToMany(
            mappedBy = "room",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(
            mappedBy = "room",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @Builder.Default
    private List<Schedule> schedules = new ArrayList<>();

    public static Room make(EnrollmentRequest request, List<String> fileUrl){
        return Room.builder()
                .enName(request.getEnName())
                .enPhoneNumber(request.getEnPhoneNumber())
                .address(Address.from(request.getLatitude(), request.getLongitude(), request.getRoadName()))
                .account(request.getAccount())
                .maxPeople(request.getMaxPeople())
                .price(request.getPrice())
                .memo(request.getMemo())
                .optionList(request.getOptionList())
                .chipList(request.getChipList())
                .photoList(fileUrl)
                .build();

    }
}