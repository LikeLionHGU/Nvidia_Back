package com.likelionhgu.nvidia.controller.request;

import com.likelionhgu.nvidia.dto.ReservationTimeDto;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReservationRequest {
    private String reName;
    private String rePhoneNumber;
    private ReservationTimeDto reservationTime;
}