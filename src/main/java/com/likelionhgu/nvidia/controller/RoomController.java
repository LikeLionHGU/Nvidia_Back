package com.likelionhgu.nvidia.controller;

import com.likelionhgu.nvidia.domain.Room;
import com.likelionhgu.nvidia.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

//    @PostMapping("/main")
//    public ResponseEntity<Room> (){}

//    @PostMapping("/search")
//    public
//
//    @PostMapping("/recommend")
//    public
//
//    @PostMapping("/recommend/detail/{roomId}")
//    public

}
