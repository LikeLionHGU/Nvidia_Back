package com.likelionhgu.nvidia.service;

import com.likelionhgu.nvidia.controller.request.*;
import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.domain.Reservation;
import com.likelionhgu.nvidia.domain.Room;
import com.likelionhgu.nvidia.domain.Schedule;
import com.likelionhgu.nvidia.dto.*;
import com.likelionhgu.nvidia.repository.AddressRepository;
import com.likelionhgu.nvidia.repository.ReservationRepository;
import com.likelionhgu.nvidia.repository.RoomRepository;
import com.likelionhgu.nvidia.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {

    AddressRepository addressRepository;
    RoomRepository roomRepository;
    ReservationRepository reservationRepository;
    ScheduleRepository scheduleRepository;

    //TODO: 탐색 범위 3km. 해당 범위 벗어나는 공실 제외하도록 구현 필요
    //TODO: 여기에서 AI를 사용해야 할 것 같다
    public List<RoomInfoDto> getRooms(AddressRequest request){
        List<RoomInfoDto> recommendRooms = null;
        Room room = roomRepository.findByAddress(Address.from(request));
        recommendRooms.add(RoomInfoDto.from(room));

        return recommendRooms;
    }

    //TODO: 여기에서 AI를 사용해야 할 것 같다
    //TODO: 금액 최댓값, 최솟값 사용자가 입력하면 해당 금액 범위 안 공실 탐색하도록 구현 필요
    //TODO: 거리 기준 필터링 → 가격 기준 필터링 → 프롬프트가 준 순서 그대로 순서 매기기 (각 추천 장소별 넘버링)
    public List<RoomInfoDto> getRoomsWithPrompt(AddressAndPromptRequest request){
        String prompt = request.getPrompt();
        List<Address> addresses = request.getAddresses();
        List<RoomInfoDto> recommendRooms = null;
        for (Address eachAddress : addresses) {
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

        //TODO: 빈 리스트 쿼리 안됨 문제 해결 필요
        List<Schedule> schedules = new ArrayList<>();
        for (EnrollmentTimeDto eachEnrollmentTime : request.getEnrollmentTimeDto()){
            Schedule eachSchedule = scheduleRepository.save(Schedule.make(eachEnrollmentTime, targetRoom));
            schedules.add(eachSchedule);
        }

        //TODO: Address를 프론트에서 어떻게 받아오는지 확인 (일단 등록은 도로명주소로만 받음)
        addressRepository.save(Address.from(request.getAddress());

        return "등록 완료";
    }

    public List<ReservationDto> accessToReservationRecords(PasswordRequest passwordRequest){
        //TODO: 같은 일자의 예약은 합치기
        List<Reservation> reservations = reservationRepository.findByRePhoneNumber(passwordRequest.getPhoneNumber());
        return reservations.stream().map(ReservationDto::from).collect(Collectors.toList());
    }

    public List<EnrollmentDto> accessToEnrollmentRecords(PasswordRequest passwordRequest){
        List<Room> rooms = roomRepository.findByPhoneNumber(passwordRequest.getPhoneNumber());
        return rooms.stream().map(EnrollmentDto::from).collect(Collectors.toList());

        //TODO: 한 날짜에 하나만 Schedule이 생성될 수 있도록 로직 확인 필요, 위 함수에서 데이터가 Schedule -> EnrollmentDto 잘 이동되는지 확인
//        List<EnrollmentDto> enrollmentDtos = new ArrayList<>();
//        for (Room eachRoom : rooms) {
//            List<Schedule> eachRoomSchedules = eachRoom.getSchedules();
//            for (Schedule eachSchedule : eachRoomSchedules){
//                if (eachSchedule){
//                    EnrollmentDto eachEnrollmentDto = EnrollmentDto.from(eachRoom);
//                    eachEnrollmentDto.setReservedDate();
//                }else{
//
//                }
//            }
//        }
//        return rooms.stream().map(rooms.stream().map(EnrollmentDto::from).collect(Collectors.toList()));
    }
}
