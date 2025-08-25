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
public class RoomInfoDto {
    private Long roomId;
    private List<String> photoList;
    private AddressDto address;
    private int maxPeople;
    private String phoneNumber;
    private String memo;
    private List<String> chipList;
    private List<String> optionList;
    private int price;

    public static RoomInfoDto from(Room room){
        return RoomInfoDto.builder()
                .roomId(room.getId())
                .photoList(room.getPhotoList())
                .address(AddressDto.from(room.getAddress()))
                .maxPeople(room.getMaxPeople())
                .phoneNumber(room.getEnPhoneNumber())
                .price(room.getPrice())
                .memo(room.getMemo())
                .chipList(room.getChipList())
                .optionList(room.getOptionList())
                .build();
    }
}