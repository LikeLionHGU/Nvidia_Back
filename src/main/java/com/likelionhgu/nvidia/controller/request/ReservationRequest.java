package com.likelionhgu.nvidia.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Getter @Setter
public class ReservationRequest {
    private String reName;
    private String rePhoneNumber;
    //TODO: API 명세서와 다름. 다른 클래스들에선 LocalDate로 저장해서 이렇게 하는 게 일관성 있다고 판단
    private LocalDate date;
    private Set<Integer> selectedTimeSlotIndex = new TreeSet<>();
}