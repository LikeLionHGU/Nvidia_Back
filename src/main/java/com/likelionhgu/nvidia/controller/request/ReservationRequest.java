package com.likelionhgu.nvidia.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Getter @Setter
public class ReservationRequest {
    private String Name;
    private String PhoneNumber;
    private String date;
    private Set<Integer> schedule = new TreeSet<>();
}