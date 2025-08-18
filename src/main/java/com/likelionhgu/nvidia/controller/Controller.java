package com.likelionhgu.nvidia.controller;

import com.likelionhgu.nvidia.controller.request.*;
import com.likelionhgu.nvidia.controller.response.EnrollmentListResponse;
import com.likelionhgu.nvidia.controller.response.ReservationListResponse;
import com.likelionhgu.nvidia.dto.EnrollmentDto;
import com.likelionhgu.nvidia.dto.ReservationDto;
import com.likelionhgu.nvidia.dto.RoomInfoDto;
import com.likelionhgu.nvidia.dto.RoomReservationInfoDto;
import com.likelionhgu.nvidia.service.Service;
import lombok.RequiredArgsConstructor;
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

    // 1.첫 화면 로딩 시 자주 찾는 장소 표시 (현재 위치 기준 근처 장소 탐색???)
    @GetMapping("/main")
    public ResponseEntity<List<RoomInfoDto>> setInitialCard(@RequestBody AddressRequest address){
        List<RoomInfoDto> recommendsDto = service.getRooms(address);
        return ResponseEntity.ok().body(recommendsDto);
    }

    // 2.주소 검색 시 결과에 해당하는 장소 추천
    @GetMapping("/search")
    public ResponseEntity<List<RoomInfoDto>> searchByWords(@RequestBody AddressRequest address){
        List<RoomInfoDto> recommendsDto = service.getRooms(address);
        return ResponseEntity.ok().body(recommendsDto);
    }

    // 3.주소 및 프롬프트 내용으로 검색 시 관련 장소 추천 (주소 자체만으로 검색 가능한가 -> 필요한 기능???)
    @GetMapping("/recommend")
    public ResponseEntity<List<RoomInfoDto>> recommendAboutPrompt(@RequestBody AddressAndPromptRequest AddressAndPrompt){
        List<RoomInfoDto> recommendsDto = service.getRoomsWithPrompt(AddressAndPrompt);
        return ResponseEntity.ok().body(recommendsDto);
    }

    // 4.추천 카드 눌렀을 때 해당 상세 모달 띄움
    @GetMapping("/recommend/detail/{roomId}")
    public ResponseEntity<RoomInfoDto> enterDetailPage(@PathVariable Long roomId){
        RoomInfoDto roomInfoDto = service.getTheRoomInfoById(roomId);
        return ResponseEntity.ok().body(roomInfoDto);
    }

    // 5.예약하러 가기 버튼 클릭 시 예약 페이지 이동
    @GetMapping("/reservation/{roomId}")
    public ResponseEntity<RoomReservationInfoDto> enterReservationPage(@PathVariable Long roomId, @RequestBody ReservationRequest request){
        RoomReservationInfoDto roomReservationInfoDto = service.getTheRoomReservationInfoById(roomId);
        return ResponseEntity.ok().body(roomReservationInfoDto);
    }

    // 5-1. 예약페이지를 띄울 때 어떤 날에 예약을 할 수 있는지 표시

    // 5-2. 캘린더에서 날짜 클릭 시 해당 날짜의 타임슬롯 표시 (일단위)

    // 6.예약 페이지에서 예약 버튼을 눌러 예약
    @PostMapping("/reservation/done/{roomId}")
    public ResponseEntity<String> doReservation(@PathVariable Long roomId, @RequestBody List<ReservationRequest> requests){
        String message = service.saveReservation(roomId, requests);
        return ResponseEntity.ok().body(message);
    }

    // 7.등록 페이지에서 등록 버튼을 눌러 등록
    @PostMapping("/enrollment/done")
    public ResponseEntity<String> doEnrollment(@RequestBody EnrollmentRequest request, @RequestParam("imageFile") MultipartFile file){
        String message = service.saveEnrollment(request, file);
        return ResponseEntity.ok().body(message);
    }

    // 8-1. 등록 기록 열람
    @GetMapping("/enrollment/confirmation")
    public ResponseEntity<List<EnrollmentListResponse>> enrollmentConfirmation(@RequestBody PasswordRequest passwordRequest){
        List<EnrollmentDto> enrollmentDtos = service.accessToEnrollmentRecords(passwordRequest);
        return ResponseEntity.ok().body(enrollmentDtos.stream().map(EnrollmentListResponse::from).collect(Collectors.toList()));
    }

    // 8-2. 예약 기록 열람
    @GetMapping("/reservation/confirmation")
    public ResponseEntity<List<ReservationListResponse>> reservationConfirmation(@RequestBody PasswordRequest passwordRequest){
        List<ReservationDto> reservationDtos = service.accessToReservationRecords(passwordRequest);
        return ResponseEntity.ok().body(reservationDtos.stream().map(ReservationListResponse::from).collect(Collectors.toList()));
    }
}
