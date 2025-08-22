package com.likelionhgu.nvidia.dto;

import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.domain.Room;
import com.likelionhgu.nvidia.domain.Schedule;
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
public class RoomReservationInfoDto {
    // RoomInfoDto 필드와 동일
    private Long roomId;
    private List<String> photoList;
    private AddressDto address;
    private int maxPeople;
    private String phoneNumber;
    private String memo;
    private List<String> chipList;
    private List<String> optionList;
    private int price;
    // // RoomInfoDto에서 추가됨
    private String account;

    public static RoomReservationInfoDto from(Room room){
        return RoomReservationInfoDto.builder()
                .roomId(room.getId())
                .photoList(room.getPhotoList())
                .address(AddressDto.from(room.getAddress()))
                .maxPeople(room.getMaxPeople())
                .phoneNumber(room.getEnPhoneNumber())
                .memo(room.getMemo())
                .chipList(room.getChipList())
                .optionList(room.getOptionList())
                .price(room.getPrice())
                .account(room.getAccount())
                .build();
    }
}
