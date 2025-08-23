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
    private String photo;        // 첫 번째 사진(없으면 null)
    private AddressDto address;  // 주소(없으면 null)
    private int maxPeople;
    private String phoneNumber;
    private int price;

    public static RoomBriefInfoDto from(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("RoomBriefInfoDto.from: room is null");
        }

        // photoList가 null이거나 비어 있을 수 있음 → 안전하게 첫 장만 추출
        String firstPhoto = null;
        List<String> photos = room.getPhotoList();
        if (photos != null && !photos.isEmpty()) {
            firstPhoto = photos.get(0);
        }

        // address가 null일 수 있음 → AddressDto.from 호출 전에 가드
        AddressDto addrDto = null;
        if (room.getAddress() != null) {
            addrDto = AddressDto.from(room.getAddress());
        }

        // room.getId()가 null인 비정상 데이터도 방어
        if (room.getId() == null) {
            throw new IllegalStateException("RoomBriefInfoDto.from: room.id is null");
        }

        return RoomBriefInfoDto.builder()
                .roomId(room.getId())
                .photo(firstPhoto)
                .address(addrDto)
                .maxPeople(room.getMaxPeople())
                .phoneNumber(room.getEnPhoneNumber())
                .price(room.getPrice())
                .build();
    }
}
