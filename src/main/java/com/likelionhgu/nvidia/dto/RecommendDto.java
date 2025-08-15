package com.likelionhgu.nvidia.dto;

import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.domain.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RecommendDto {
    private Long roomId;
    private List<String> photo;
    private Address address;
    private int maxPeople;
    private String phoneNumber;
    private int price;

    public static RecommendDto from(Room room){
        return RecommendDto.builder()
                .roomId(room.getId())
                .photo(room.getPhotoList())
                .address(room.getAddress())
                .maxPeople(room.getMaxPeople())
                .phoneNumber(room.getEnPhoneNumber())
                .price(room.getPrice())
                .build();
    }
}