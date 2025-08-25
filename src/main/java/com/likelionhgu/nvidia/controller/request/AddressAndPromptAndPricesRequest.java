package com.likelionhgu.nvidia.controller.request;

import com.likelionhgu.nvidia.dto.AddressDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class AddressAndPromptAndPricesRequest {
    private List<AddressDto> addressList;
    private String prompt;
    private int maxPrice;
    private int minPrice;
}