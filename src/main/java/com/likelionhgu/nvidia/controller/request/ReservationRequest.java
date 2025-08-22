package com.likelionhgu.nvidia.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Getter @Setter
//TODO: API 명세서에는 변수명에 "re"가 없음. 수정해야 하는지 확인 필요
public class ReservationRequest {
    private String Name;
    private String PhoneNumber;
    //TODO: API 명세서와 다름. 다른 클래스들에선 LocalDate로 저장해서 이렇게 하는 게 일관성 있다고 판단
    private String date;
    private Set<Integer> schedule = new TreeSet<>();
}