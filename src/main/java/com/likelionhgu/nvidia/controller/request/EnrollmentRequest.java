package com.likelionhgu.nvidia.controller.request;

import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.dto.EnrollmentTimeDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class EnrollmentRequest {
    private String enName;
    private String enPhoneNumber;
    //TODO: 다른 곳에선 모두 Address 타입으로 위도, 경도도 받는데 여기도 그렇게 할지 논의 필요
    private Address address;
    private String account;
    private int maxPeople;
    private int price;
    private String memo;
    private List<String> optionList;
    private List<String> chipList;
    //TODO: 프론트에서 등록자가 선택한 시간 넘겨주도록 하기
    private List<EnrollmentTimeDto> enrollmentTimeDto;

}
