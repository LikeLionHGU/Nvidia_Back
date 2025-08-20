package com.likelionhgu.nvidia.controller.response;

import com.likelionhgu.nvidia.dto.CoordinateAddressDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class MiddleAddressResponse {
    private Double latitude;
    private Double longitude;

    public static MiddleAddressResponse from(CoordinateAddressDto coordinateAddressDto){
        return MiddleAddressResponse.builder()
                .latitude(coordinateAddressDto.getLatitude())
                .longitude(coordinateAddressDto.getLongitude())
                .build();
    }
}