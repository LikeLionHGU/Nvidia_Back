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
public class RoomInfoDto {
    private Long roomId;
    private List<String> photo;
    private AddressDto address;
    private int maxPeople;
    private String phoneNumber;
    private int price;
    private String memo;
    private List<String> chipList;
    private List<String> optionList;

    public static RoomInfoDto from(Room room){
        return RoomInfoDto.builder()
                .roomId(room.getId())
                .photo(room.getPhotoList())
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