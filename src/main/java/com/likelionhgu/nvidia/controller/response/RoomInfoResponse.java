package com.likelionhgu.nvidia.controller.response;

import com.likelionhgu.nvidia.domain.Room;
import com.likelionhgu.nvidia.dto.AddressDto;
import com.likelionhgu.nvidia.dto.RoomInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RoomInfoResponse {
    private Long roomId;
    private List<String> photo;
    private AddressDto address;
    private int maxPeople;
    private String phoneNumber;
    private String memo;
    private List<String> chipList;
    private List<String> optionList;
    private int price;

    public static RoomInfoResponse from(RoomInfoDto roomInfoDto){
        return RoomInfoResponse.builder()
                .roomId(roomInfoDto.getRoomId())
                .photo(roomInfoDto.getPhoto())
                .address(roomInfoDto.getAddress())
                .maxPeople(roomInfoDto.getMaxPeople())
                .phoneNumber(roomInfoDto.getPhoneNumber())
                .price(roomInfoDto.getPrice())
                .memo(roomInfoDto.getMemo())
                .chipList(roomInfoDto.getChipList())
                .optionList(roomInfoDto.getOptionList())
                .build();
    }
}