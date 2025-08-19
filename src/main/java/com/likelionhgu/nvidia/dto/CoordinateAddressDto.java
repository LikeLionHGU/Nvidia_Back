package com.likelionhgu.nvidia.dto;

import com.likelionhgu.nvidia.domain.Address;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class CoordinateAddressDto {
    private double latitude;
    private double longitude;

    public static CoordinateAddressDto from(double latitude, double longitude){
        return CoordinateAddressDto.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    public static CoordinateAddressDto from(CoordinateAddressDto addressDto){
        return CoordinateAddressDto.builder()
                .latitude(addressDto.getLatitude())
                .longitude(addressDto.getLongitude())
                .build();
    }
}
