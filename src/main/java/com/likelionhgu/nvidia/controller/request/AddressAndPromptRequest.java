package com.likelionhgu.nvidia.controller.request;

import com.likelionhgu.nvidia.domain.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class AddressAndPromptRequest {
    private List<Address> addresses;
    private String prompt;
}