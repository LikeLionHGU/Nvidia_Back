package com.likelionhgu.nvidia.domain;

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
                .roadName(null)
                .build();
    }

    //TODO: AddressAndPromptRequest 안의 Address 해결하기
//    public static Address from(AddressAndPromptRequest addressAndPromptRequest){
//        return Address.builder()
//                .latitude(addressAndPromptRequest.getAddresses().getLatitude())
//                .longitude(addressAndPromptRequest.getAddresses().getLongitude())
//                .roadName(addressAndPromptRequest.getAddresses().getRoadName())
//                .build();
//    }

    @OneToOne(mappedBy = "address")
    private Room room;
}
