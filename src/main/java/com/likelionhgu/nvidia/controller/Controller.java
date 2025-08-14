package com.likelionhgu.nvidia.controller;

import com.likelionhgu.nvidia.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {
    private final RoomService roomService;

//    @PostMapping("/main")
//    public ResponseEntity<Room> (){}

    // response, request -> 먼저
    // 컨트롤러 -> 서비스 -> 데이터베이스 접근 필요 시 의존성 주입 -> 레포지토지

//    @PostMapping("/search")
//    public
//
//    @PostMapping("/recommend")
//    public
//
//    @PostMapping("/recommend/detail/{roomId}")
//    public

//    @PostMapping("/reservation/{roomId}")

//    @PostMapping("/reservation/done/{roomId}")

//    @PostMapping("/reservationrecommend")

}
