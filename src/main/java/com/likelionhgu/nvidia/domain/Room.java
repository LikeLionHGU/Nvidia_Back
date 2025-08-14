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

    // 문자열 리스트는 ElementCollection로 별도 테이블에 매핑
    @ElementCollection
    @CollectionTable(name = "room_options", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "option_name")
    private List<String> optionList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "room_chips", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "chip_name")
    private List<String> chipList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "room_photos", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "photo_url")
    private List<String> photoList = new ArrayList<>();
}
