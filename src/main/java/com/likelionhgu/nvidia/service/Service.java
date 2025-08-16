package com.likelionhgu.nvidia.service;

import com.likelionhgu.nvidia.controller.request.*;
import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.domain.Reservation;
import com.likelionhgu.nvidia.domain.Room;
import com.likelionhgu.nvidia.domain.Schedule;
import com.likelionhgu.nvidia.dto.EnrollmentTimeDto;
import com.likelionhgu.nvidia.dto.RoomInfoDto;
import com.likelionhgu.nvidia.dto.RoomReservationInfoDto;
import com.likelionhgu.nvidia.repository.AddressRepository;
import com.likelionhgu.nvidia.repository.ReservationRepository;
import com.likelionhgu.nvidia.repository.RoomRepository;
import com.likelionhgu.nvidia.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {

    AddressRepository addressRepository;
    RoomRepository roomRepository;
    ReservationRepository reservationRepository;
    ScheduleRepository scheduleRepository;

    public List<RoomInfoDto> getRooms(AddressRequest request){
            //TODO: 여기에서 AI를 사용해야 할 것 같다
        List<RoomInfoDto> recommendRooms = null;
        Room room = roomRepository.findByAddress(Address.from(request));
        recommendRooms.add(RoomInfoDto.from(room));

        return recommendRooms;
    }

    public List<RoomInfoDto> getRoomsWithPrompt(AddressAndPromptRequest request){
        String prompt = request.getPrompt();
        List<Address> addresses = request.getAddresses();
        List<RoomInfoDto> recommendRooms = null;
        for (Address eachAddress : addresses) {
            //TODO: 여기에서 AI를 사용해야 할 것 같다
            Room room = roomRepository.findByAddress(eachAddress);
            recommendRooms.add(RoomInfoDto.from(room));
        }
        return recommendRooms;
    }

    public RoomInfoDto getTheRoomInfoById(Long roomId){
        Room room = roomRepository.findByRoomId(roomId);
        return RoomInfoDto.from(room);
    }

    public RoomReservationInfoDto getTheRoomReservationInfoById(Long roomId){
        Room room = roomRepository.findByRoomId(roomId);
        return RoomReservationInfoDto.from(room);
    }


    //TODO: 시간 슬롯 넘겨주는 로직 수정 및 스케줄 Entity 재검토 필요

    public String saveReservation(Long roomId, ReservationRequest request){
        Room room = roomRepository.findByRoomId(roomId);
        Schedule schedule = scheduleRepository.findByDate(request.getDate());
        schedule.setRePhoneNumber(request.getRePhoneNumber());

        if (room != null) {
            Reservation reservation = Reservation.from(room, request);
            reservationRepository.save(reservation);
        }
        return "예약 완료";
    }

    public String saveEnrollment(EnrollmentRequest request, MultipartFile file){
        //TODO: 파일 AWS 서버에 올리는 로직 추가

        // 파일 변환 방법 잊어버림 -> help 자이온!
//        file
        List<String> fileUrl;

        Room targetRoom = roomRepository.save(Room.make(request, fileUrl));

        //TODO: LocalDate으로??
        List<Schedule> schedules = null;
        for (EnrollmentTimeDto eachEnrollmentTime : request.getEnrollmentTimeDto()){
            Schedule eachSchedule = scheduleRepository.save(Schedule.make(eachEnrollmentTime, targetRoom));
            schedules.add(eachSchedule);
        }

        //TODO: Address를 프론트에서 어떻게 받아오는지 확인 (일단 등록은 도로명주소로만 받음)
        addressRepository.save(Address.from(request.getAddress()));

        return "등록 완료";
    }

    public List<RoomInfoDto> accessToRecords(PasswordRequest passwordRequest){

    }
}
