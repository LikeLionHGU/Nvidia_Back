package com.likelionhgu.nvidia.controller;

import com.likelionhgu.nvidia.controller.request.*;
import com.likelionhgu.nvidia.controller.response.*;
import com.likelionhgu.nvidia.dto.*;
import com.likelionhgu.nvidia.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final Service service;

    // 개발 순서
    // response, request -> 먼저
    // 컨트롤러 -> 서비스 -> 데이터베이스 접근 필요 시 의존성 주입 -> 레포지토지

    // 1.첫 화면 로딩 시 자주 찾는 장소 표시 (현재 위치 기준 근처 장소 탐색)
    @PostMapping("/main")
    public ResponseEntity<List<RoomBriefInfoDto>> setInitialCard(@RequestBody AddressRequest address){
        List<RoomBriefInfoDto> recommendsDto = service.getRooms(address);
        return ResponseEntity.ok().body(recommendsDto);
    }

    // 2-1. 좌측 사이드바 검색창에서 Step1 끝낸 직후 중간 위치 전송
    @PostMapping("/search/middle")
    public ResponseEntity<MiddleAddressResponse> searchMiddle(@RequestBody AddressesForMiddleRequest request){
        CoordinateAddressDto coordinateAddressDto = service.calculateMidpoint(request);
        return ResponseEntity.ok().body(MiddleAddressResponse.from(coordinateAddressDto));
    }

    // 3.주소 및 프롬프트 내용으로 검색 시 관련 장소 추천
    @PostMapping("/recommend")
    public ResponseEntity<List<RoomBriefInfoResponse>> recommendAboutPrompt(@RequestBody AddressAndPromptAndPricesRequest AddressAndPrompt){
        List<RoomBriefInfoDto> recommendsDtos = service.getRoomsWithPrompt(AddressAndPrompt);
        return ResponseEntity.ok().body(recommendsDtos.stream().map(RoomBriefInfoResponse::from).toList());
    }

    // 4.추천 카드 눌렀을 때 해당 상세 모달 띄움
    @GetMapping("/recommend/detail/{roomId}")
    public ResponseEntity<RoomInfoResponse> enterDetailPage(@PathVariable Long roomId){
        RoomInfoDto roomInfoDto = service.getTheRoomInfoById(roomId);
        return ResponseEntity.ok().body(RoomInfoResponse.from(roomInfoDto));
    }

    // 5.예약하러 가기 버튼 클릭 시 예약 페이지 이동
    @PostMapping("/reservation/{roomId}")
    public ResponseEntity<RoomReservationInfoResponse> enterReservationPage(@PathVariable Long roomId){
        RoomReservationInfoDto roomReservationInfoDto = service.getTheRoomReservationInfoById(roomId);
        return ResponseEntity.ok().body(RoomReservationInfoResponse.from(roomReservationInfoDto));
    }

    // 5-1. 예약페이지를 띄울 때 어떤 날에 예약을 할 수 있는지 표시
    @PostMapping("/reservation/available/days/{roomId}")
    public ResponseEntity<AvailableDaysResponse> availableDaysOfTheMonth(@PathVariable Long roomId, @RequestBody MonthRequest request){
        AvailableDaysDto availableDaysDto = service.getAvailableDays(roomId, request);
        return ResponseEntity.ok().body(AvailableDaysResponse.from(availableDaysDto));
    }

    // 5-2. 캘린더에서 날짜 클릭 시 해당 날짜의 타임슬롯 표시 (일단위)
    @PostMapping("/reservation/available/timeslots/{roomId}")
    public  ResponseEntity<AvailableTimeSlotsResponse> availableTimeSlotsOfTheDay(@PathVariable Long roomId, @RequestBody MonthAndDayRequest request){
        AvailableTimeSlotsDto availableTimeSlotsDto = service.getAvailableTimeSlots(roomId, request);
        return ResponseEntity.ok().body(AvailableTimeSlotsResponse.from(availableTimeSlotsDto));
    }

    // 6.예약 페이지에서 예약 버튼을 눌러 예약
    @PostMapping("/reservation/done/{roomId}")
    public ResponseEntity<String> doReservation(@PathVariable Long roomId, @RequestBody List<ReservationRequest> requests){
        String message = service.saveReservation(roomId, requests);
        return ResponseEntity.ok().body(message);
    }

    // 7.등록 페이지에서 등록 버튼을 눌러 등록
    @PostMapping(value="/enrollment/done", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> doEnrollment(
            @RequestPart("request") EnrollmentRequest request,
            @RequestPart("imageFile") List<MultipartFile> files){
        String message = service.saveEnrollment(request, files);
        return ResponseEntity.ok().body(message);
    }

    // 8-1. 등록 기록 열람
    @PostMapping("/enrollment/confirmation")
    public ResponseEntity<List<EnrollmentListResponse>> enrollmentConfirmation(@RequestBody PasswordRequest passwordRequest){
        List<EnrollmentDto> enrollmentDtos = service.accessToEnrollmentRecords(passwordRequest);
        return ResponseEntity.ok().body(enrollmentDtos.stream().map(EnrollmentListResponse::from).collect(Collectors.toList()));
    }

    // 8-2. 예약 기록 열람
    @PostMapping("/reservation/confirmation")
    public ResponseEntity<List<ReservationListResponse>> reservationConfirmation(@RequestBody PasswordRequest passwordRequest){
        List<ReservationDto> reservationDtos = service.accessToReservationRecords(passwordRequest);
        return ResponseEntity.ok().body(reservationDtos.stream().map(ReservationListResponse::from).collect(Collectors.toList()));
    }
}
