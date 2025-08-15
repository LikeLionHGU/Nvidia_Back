package com.likelionhgu.nvidia.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddressAndPromptRequest {
    private String roadName;
    private Double latitude;
    private Double longitude;
    private String prompt;
}