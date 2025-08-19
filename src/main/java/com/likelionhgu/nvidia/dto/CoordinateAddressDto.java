package com.likelionhgu.nvidia.dto;

import com.likelionhgu.nvidia.controller.request.AddressRequest;
import com.likelionhgu.nvidia.controller.request.AddressesForMiddleRequest;
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

    public static CoordinateAddressDto from(AddressRequest addressRequest){
        return CoordinateAddressDto.builder()
                .latitude(addressRequest.getLatitude())
                .longitude(addressRequest.getLongitude())
                .build();
    }

    public static CoordinateAddressDto from(Address address){
        return CoordinateAddressDto.builder()
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .build();
    }
}
