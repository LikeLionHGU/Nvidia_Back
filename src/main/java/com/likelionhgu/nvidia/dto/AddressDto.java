package com.likelionhgu.nvidia.dto;

import com.likelionhgu.nvidia.controller.request.AddressRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressDto {
    private double latitude;
    private double longitude;

    public static AddressDto from(AddressRequest addressRequest){
        return AddressDto.builder()
                .latitude(addressRequest.getLatitude())
                .longitude(addressRequest.getLongitude())
                .build();
    }

    public static AddressDto from(double latitude, double longitude){
        return AddressDto.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
