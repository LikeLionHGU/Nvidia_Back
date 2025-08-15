package com.likelionhgu.nvidia.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddressRequest {
    private String roadName;
    private Double latitude;
    private Double longitude;
}