package com.likelionhgu.nvidia.service;

import com.likelionhgu.nvidia.controller.request.AddressAndPromptRequest;
import com.likelionhgu.nvidia.controller.request.AddressRequest;
import com.likelionhgu.nvidia.controller.request.ReservationRequest;
import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.domain.Reservation;
import com.likelionhgu.nvidia.domain.Room;
import com.likelionhgu.nvidia.dto.RecommendDto;
import com.likelionhgu.nvidia.repository.AddressRepository;
import com.likelionhgu.nvidia.repository.ReservationRepository;
import com.likelionhgu.nvidia.repository.RoomRepository;
import com.likelionhgu.nvidia.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {

    AddressRepository addressRepository;
    RoomRepository roomRepository;
    ReservationRepository reservationRepository;
    ScheduleRepository scheduleRepository;

    //TODO: Address 사용 방법에 의문이 든다. Address를 Dto가 아닌 Entity로 만든 이유는 Repositoty를 만들어 AI 검색 시 위치 기반 추천해주기 위함인가?
    //TODO: 단순 위치로 공간 정보 받아올 때는 AddressRepository 접근 없이 하는 게 효율적인가?

    public List<RecommendDto> getRooms(AddressRequest request){
        roomRepository.findByAddress(Address.from(request));
        //TODO: 여기에서 AI를 사용해야 할 것 같다
        List<RecommendDto> recommendsDto = Rooms.stream().map(RecommendDto::from).collect(Collectors.toList());
        return recommendsDto;
    }

    public List<RecommendDto> getRoomsWithPrompt(AddressAndPromptRequest request){
        roomRepository.findByAddress(Address.from(request));
        //TODO: 여기에서 AI를 사용해야 할 것 같다

    }

    public List<RecommendDto> getTheRoomById(Long roomId){
        roomRepository.findById(roomId);
    }

    public String saveReservation(Long roomId, ReservationRequest request){
        Room room = roomRepository.findByRoomId(roomId);
        if (room != null) {
            Reservation reservation = Reservation.from(room, request);
            reservationRepository.save(reservation);
        }
        return "예약 완료";
    }

    public List<RecommendDto> saveEnrollment(roomId){

    }
    public List<RecommendDto> accessToRecords(passwordRequest){

    }
}
