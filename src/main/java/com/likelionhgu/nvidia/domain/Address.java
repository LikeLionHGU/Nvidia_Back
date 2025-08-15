package com.likelionhgu.nvidia.domain;

import com.likelionhgu.nvidia.controller.request.AddressAndPromptRequest;
import com.likelionhgu.nvidia.controller.request.AddressRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Table(name = "address")
public class Address {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double latitude;
    private double longitude;
    private String roadName;

    public static Address from(AddressRequest addressRequest){
        return Address.builder()
                .latitude(addressRequest.getLatitude())
                .longitude(addressRequest.getLongitude())
                .roadName(addressRequest.getRoadName())
                .build();
    }

    public static Address from(AddressAndPromptRequest addressAndPromptRequest){
        return Address.builder()
                .latitude(addressAndPromptRequest.getLatitude())
                .longitude(addressAndPromptRequest.getLongitude())
                .roadName(addressAndPromptRequest.getRoadName())
                .build();

    }
}
