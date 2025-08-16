package com.likelionhgu.nvidia.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.TreeSet;

@Getter @Setter
public class ReservationRequest {
    private String reName;
    private String rePhoneNumber;
    private String date;
    private Set<Integer> selectedTimeSlot = new TreeSet<>();
}