package com.likelionhgu.nvidia.controller.response;

import com.likelionhgu.nvidia.dto.AddressDto;
import com.likelionhgu.nvidia.dto.RoomBriefInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RoomBriefInfoResponse {
    private Long roomId;
    private String photo;
    private AddressDto address;
    private int maxPeople;
    private String phoneNumber;
    private int price;

    public static RoomBriefInfoResponse from(RoomBriefInfoDto roomBriefInfoDto){
        return RoomBriefInfoResponse.builder()
                .roomId(roomBriefInfoDto.getRoomId())
                .photo(roomBriefInfoDto.getPhoto())
                .address(roomBriefInfoDto.getAddress())
                .maxPeople(roomBriefInfoDto.getMaxPeople())
                .phoneNumber(roomBriefInfoDto.getPhoneNumber())
                .price(roomBriefInfoDto.getPrice())
                .build();
    }
}