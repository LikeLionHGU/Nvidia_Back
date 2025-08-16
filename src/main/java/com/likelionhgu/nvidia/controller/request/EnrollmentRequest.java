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
    //TODO: 프론트에서 등록자가 선택한 시간 넘겨주도록 하기
    private List<EnrollmentTimeDto> enrollmentTimeDto;

}
