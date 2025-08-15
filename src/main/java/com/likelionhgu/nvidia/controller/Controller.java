package com.likelionhgu.nvidia.controller;

import com.likelionhgu.nvidia.controller.request.AddressAndPromptRequest;
import com.likelionhgu.nvidia.controller.request.AddressRequest;
import com.likelionhgu.nvidia.controller.request.PasswordRequest;
import com.likelionhgu.nvidia.controller.request.ReservationRequest;
import com.likelionhgu.nvidia.domain.Reservation;
import com.likelionhgu.nvidia.domain.Room;
import com.likelionhgu.nvidia.dto.ReAndEnDto;
import com.likelionhgu.nvidia.dto.RecommendDto;
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

    // response, request -> 먼저
    // 컨트롤러 -> 서비스 -> 데이터베이스 접근 필요 시 의존성 주입 -> 레포지토지

    @GetMapping("/main")
    public ResponseEntity<List<RecommendDto>> setInitialCard(@RequestBody AddressRequest address){
        List<RecommendDto> recommendsDto = service.getRooms(address);
        return ResponseEntity.ok().body(recommendsDto);
    }

    //TODO: Post 말고 Get 아닌가?
    @PostMapping("/search")
    public ResponseEntity<List<RecommendDto>> searchByWords(@RequestBody AddressRequest address){
        List<RecommendDto> recommendsDto = service.getRooms(address);
        return ResponseEntity.ok().body(recommendsDto);
    }

    //TODO: Post 말고 Get 아닌가?
    @PostMapping("/recommend")
    public ResponseEntity<List<RecommendDto>> recommendAboutPrompt(@RequestBody AddressAndPromptRequest AddressAndPrompt){
        List<RecommendDto> recommendsDto = service.getRoomsWithPrompt(AddressAndPrompt);
        return ResponseEntity.ok().body(recommendsDto);
    }

    @GetMapping("/recommend/detail/{roomId}")
    public ResponseEntity<RecommendDto> enterDetailPage(@PathVariable Long roomId){
        RecommendDto recommendDto = service.getTheRoomById(roomId);
        return ResponseEntity.ok().body(recommendDto);
    }

    @GetMapping("/reservation/{roomId}")
    public ResponseEntity<RecommendDto> enterReservationPage(@PathVariable Long roomId, @RequestBody ReservationRequest request){
        RecommendDto recommendDto = service.getTheRoomById(roomId);
        return ResponseEntity.ok().body(recommendDto);
    }

    @PostMapping("/reservation/done/{roomId}")
    public String doReservation(@PathVariable Long roomId, @RequestBody ReservationRequest request){
        String message = service.saveReservation(roomId, request);
        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/enrollment/done")
    public String doEnrollment(@RequestParam("imageFile") MultipartFile file){
        String message = service.saveEnrollment(roomId);
        return ResponseEntity.ok().body(message);
    }

    //TODO: Post 말고 Get 아닌가?
    @PostMapping("/reservation/confirmation")
    public ResponseEntity<ReAndEnDto> checkConfirmation(@RequestBody PasswordRequest passwordRequest){
        ReAndEnDto ReAndEns = service.accessToRecords(passwordRequest);
        return ResponseEntity.ok().body(ReAndEns);
    }

}
