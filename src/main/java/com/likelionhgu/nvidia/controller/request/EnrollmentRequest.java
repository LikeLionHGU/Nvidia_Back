package com.likelionhgu.nvidia.controller.request;

import com.likelionhgu.nvidia.dto.EnrollmentTimeDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class EnrollmentRequest {
    private String enName;
    private String enPhoneNumber;
    private String address;
    private String account;
    private int maxPeople;
    private int price;
    private String memo;
    private List<String> optionList;
    private List<String> chipList;
    private EnrollmentTimeDto enrollmentTimeDto;

}
