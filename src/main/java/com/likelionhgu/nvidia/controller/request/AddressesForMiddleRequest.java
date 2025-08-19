package com.likelionhgu.nvidia.controller.request;

import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.dto.CoordinateAddressDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddressesForMiddleRequest {
    private List<CoordinateAddressDto> addressList;
}
