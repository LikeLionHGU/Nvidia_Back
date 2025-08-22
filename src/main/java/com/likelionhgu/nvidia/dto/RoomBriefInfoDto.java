package com.likelionhgu.nvidia.dto;

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
public class RoomBriefInfoDto {
    private Long roomId;
    private String photo;
    private AddressDto address;
    private int maxPeople;
    private String phoneNumber;
    private int price;

    public static RoomBriefInfoDto from(Room room){
        return RoomBriefInfoDto.builder()
                .roomId(room.getId())
                .photo(room.getPhotoList().get(0))
                .address(AddressDto.from(room.getAddress()))
                .maxPeople(room.getMaxPeople())
                .phoneNumber(room.getEnPhoneNumber())
                .price(room.getPrice())
                .build();
    }
}