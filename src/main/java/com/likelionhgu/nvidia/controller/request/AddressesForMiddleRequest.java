package com.likelionhgu.nvidia.controller.request;

import com.likelionhgu.nvidia.dto.AddressDto;
import com.likelionhgu.nvidia.dto.CoordinateAddressDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class AddressesForMiddleRequest {
    private List<CoordinateAddressDto> addressList;

    public static AddressesForMiddleRequest from(List<AddressDto> addressDto){
        return AddressesForMiddleRequest.builder()
                .addressList(addressDto.stream().map(CoordinateAddressDto::from).toList())
                .build();
    }
}
