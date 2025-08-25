package com.likelionhgu.nvidia.dto;

import com.likelionhgu.nvidia.domain.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressDto {
    private String roadName;
    private double latitude;
    private double longitude;

    public static AddressDto from(Address address){
        return AddressDto.builder()
                .roadName(address.getRoadName())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .build();
    }
}
