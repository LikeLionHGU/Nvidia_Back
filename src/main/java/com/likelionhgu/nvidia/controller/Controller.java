package com.likelionhgu.nvidia.controller;

import com.likelionhgu.nvidia.controller.request.*;
import com.likelionhgu.nvidia.dto.ReAndEnDto;
import com.likelionhgu.nvidia.dto.RoomInfoDto;
import com.likelionhgu.nvidia.dto.RoomReservationInfoDto;
import com.likelionhgu.nvidia.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    // 6.예약 페이지에서 예약 버튼을 눌러 예약
    @PostMapping("/reservation/done/{roomId}")
    public ResponseEntity<String> doReservation(@PathVariable Long roomId, @RequestBody ReservationRequest request){
        String message = service.saveReservation(roomId, request);
        return ResponseEntity.ok().body(message);
    }

    // 7.등록 페이지에서 등록 버튼을 눌러 등록
    @PostMapping("/enrollment/done")
    public ResponseEntity<String> doEnrollment(@RequestBody EnrollmentRequest request, @RequestParam("imageFile") MultipartFile file){
        String message = service.saveEnrollment(request, file);
        return ResponseEntity.ok().body(message);
    }

    // 8.예약/등록 확인 모달에서 전화번호 입력 후 확인 버튼 클릭으로 확인 페이지 이동
    @GetMapping("/reservation/confirmation")
    public ResponseEntity<ReAndEnDto> checkConfirmation(@RequestBody PasswordRequest passwordRequest){
        ReAndEnDto ReAndEns = service.accessToRecords(passwordRequest);
        return ResponseEntity.ok().body(ReAndEns);
    }


//TODO: 등록 확인, 예약 확인 페이지(탭) 분리 시 api도 아래와 같이 분리

//    @GetMapping("/reservation/confirmation")
//    public ResponseEntity<ReAndEnDto> checkConfirmation(@RequestBody PasswordRequest passwordRequest){
//        ReAndEnDto ReAndEns = service.accessToRecords(passwordRequest);
//        return ResponseEntity.ok().body(ReAndEns);
//    }
//    @GetMapping("/enrollment/confirmation")
//    public ResponseEntity<ReAndEnDto> checkConfirmation(@RequestBody PasswordRequest passwordRequest){
//        ReAndEnDto ReAndEns = service.accessToRecords(passwordRequest);
//        return ResponseEntity.ok().body(ReAndEns);
//    }
}
