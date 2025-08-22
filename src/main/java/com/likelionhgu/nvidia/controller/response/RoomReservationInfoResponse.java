package com.likelionhgu.nvidia.controller.response;

import com.likelionhgu.nvidia.domain.Room;
import com.likelionhgu.nvidia.dto.AddressDto;
import com.likelionhgu.nvidia.dto.RoomReservationInfoDto;
import com.likelionhgu.nvidia.dto.ScheduleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RoomReservationInfoResponse {
    private Long roomId;
    private List<String> photoList;
    private AddressDto address;
    private int maxPeople;
    private String phoneNumber;
    private String memo;
    private List<String> chipList;
    private List<String> optionList;
    private int price;
    private String account;

    public static RoomReservationInfoResponse from(RoomReservationInfoDto roomReservationInfoDto){
        return RoomReservationInfoResponse.builder()
                .roomId(roomReservationInfoDto.getRoomId())
                .photoList(roomReservationInfoDto.getPhotoList())
                .address(roomReservationInfoDto.getAddress())
                .maxPeople(roomReservationInfoDto.getMaxPeople())
                .phoneNumber(roomReservationInfoDto.getPhoneNumber())
                .memo(roomReservationInfoDto.getMemo())
                .chipList(roomReservationInfoDto.getChipList())
                .optionList(roomReservationInfoDto.getOptionList())
                .price(roomReservationInfoDto.getPrice())
                .account(roomReservationInfoDto.getAccount())
                .build();
    }
}
