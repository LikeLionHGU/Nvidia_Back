package com.likelionhgu.nvidia.dto;

import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.domain.Room;
import com.likelionhgu.nvidia.domain.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RoomReservationInfoDto {
    private Long roomId;
    private String memo;
    private String account;
    private int maxPeople;
    private List<Schedule> timeTable;

    public static RoomReservationInfoDto from(Room room){
        return RoomReservationInfoDto.builder()
                .roomId(room.getId())
                .memo(room.getMemo())
                .account(room.getAccount())
                .maxPeople(room.getMaxPeople())
                .timeTable(room.getSchedules())
                .build();
    }
}
