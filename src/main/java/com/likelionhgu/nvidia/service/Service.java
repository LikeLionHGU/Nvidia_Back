package com.likelionhgu.nvidia.service;

import com.likelionhgu.nvidia.config.S3Service;
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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {

    private final S3Service s3Service;

    private final AddressRepository addressRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final ScheduleRepository scheduleRepository;

    //TODO: [AI 적용] 탐색 범위 3km. 해당 범위 벗어나는 공실 제외하도록 구현 필요
    //TODO: 여기에서 AI를 사용해야 할 것 같다

    // request에 해당하는 공실을 repository에서 찾는다.
    public List<RoomInfoDto> getRooms(AddressRequest request){
        double radiusInKm = 3.0;
        List<Address> nearbyAddresses = addressRepository.findByLocationWithinRadius(request.getLatitude(), request.getLongitude(), radiusInKm);

        return nearbyAddresses.stream().map(Address::getRoom).map(RoomInfoDto::from).toList();
    }

    // 받은 주소 리스트들의 중간 주소를 계산한다.
    public CoordinateAddressDto calculateMidpoint(AddressesForMiddleRequest request){
        int number = request.getAddressList().size();

        if (number == 1){
            return request.getAddressList().get(0);
        }

        double totalLatitude = 0;
        double totalLongitude = 0;
        for (CoordinateAddressDto eachAddressDto: request.getAddressList()){
            totalLatitude += eachAddressDto.getLatitude();
            totalLongitude += eachAddressDto.getLongitude();
        }
        return CoordinateAddressDto.from(totalLatitude / number, totalLongitude / number);
    }

    // request에 해당하는 공실 여러 개를 repository에서 찾고 프롬프트 조건으로 필터링한다.
    //TODO: 여기에서 AI를 사용해야 할 것 같다
    //TODO: 금액 최댓값, 최솟값 사용자가 입력하면 해당 금액 범위 안 공실 탐색하도록 구현 필요
    //TODO: 거리 기준 필터링 → 가격 기준 필터링 → 프롬프트가 준 순서 그대로 순서 매기기 (각 추천 장소별 넘버링)
    public List<RoomInfoDto> getRoomsWithPrompt(AddressAndPromptAndPricesRequest request){
        String prompt = request.getPrompt();
        //TODO: request 안에 maxPrice, minPrice 있으니 사용 필요

        List<Address> addresses = request.getAddresses();
        List<RoomInfoDto> recommendRooms = new ArrayList<>();
        for (Address eachAddress : addresses) {
            Room room = roomRepository.findByAddressAndPriceBetween(eachAddress, request.getMinPrice(), request.getMaxPrice());
            recommendRooms.add(RoomInfoDto.from(room));
        }
        return recommendRooms;
    }

    // 자세히보기 클릭 시 띄우는 모달의 정보를 불러온다.
    public RoomInfoDto getTheRoomInfoById(Long roomId){
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Room not found"));
        return RoomInfoDto.from(room);
    }

    // 예약 페이지의 정보들을 불러온다.
    public RoomReservationInfoDto getTheRoomReservationInfoById(Long roomId){
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Room not found"));
        return RoomReservationInfoDto.from(room);
    }

    // 입력받은(캘린더에서 선택한) 달의 등록된 일들을 불러온다.
    public AvailableDaysDto getAvailableDays(Long roomId, MonthRequest request){
        int thisYear = LocalDate.now().getYear();
        LocalDate startDate = LocalDate.of(thisYear, request.getMonth(), 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        List<Schedule> schedules = scheduleRepository.findByRoomIdAndDateBetween(roomId, startDate, endDate);

        return AvailableDaysDto.from(schedules);
    }

    // 입력받은(캘린더에서 선택한) 일의 등록된 시간 슬롯을 불러온다.
    public AvailableTimeSlotsDto getAvailableTimeSlots(Long roomId, MonthAndDayRequest request){
        int thisYear = LocalDate.now().getYear();
        LocalDate targetDate = LocalDate.of(thisYear, request.getMonth(), request.getDay());
        Schedule schedules = scheduleRepository.findByRoomIdAndDate(roomId, targetDate);

        return AvailableTimeSlotsDto.from(schedules);
    }



    // 예약 페이지에 입력된 정보들을 예약 기록(Reservation)으로 저장한다.
    //TODO: 시간 슬롯 넘겨주는 로직 수정 및 스케줄 Entity 재검토 필요
    // 현재는 한 날짜만 받는 걸로 되어 있음. 반복 전송이 아닌 여러 날짜를 한번에 보낸다면 수정 필요 (일단 프론트에게 API 명세서 댓글로 물어봄)
    public String saveReservation(Long roomId, List<ReservationRequest> requests){
        for (ReservationRequest eachRequest : requests){
            Schedule schedule = scheduleRepository.findByRoomIdAndDate(roomId, eachRequest.getDate());
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Room not found"));
            //TODO: room에서 바로 schedule 접근해서 rePhoneNumber를 수정할 수 있다면 로직 수정 필요

            if (room != null) {
                Reservation reservation = Reservation.from(room, eachRequest);
                reservationRepository.save(reservation);
                schedule.setRePhoneNumber(eachRequest.getRePhoneNumber());
            }
        }

        return "예약 완료";
    }

    // 등록 페이지에 입력된 정보들을 등록 기록(Room, Address, Schedule 각각)으로 저장한다.
    //TODO: 한 날짜 시간 슬롯 전체 선택 기능도 구현 필요
    public String saveEnrollment(EnrollmentRequest request, MultipartFile file){
        String uploadUrl = null;
        Map<String, String> roomPhotoMap = new HashMap<>();

        try {
            uploadUrl = s3Service.uploadFiles(file, "roomPhoto/");
            roomPhotoMap= Map.of("uploadUrl",uploadUrl);
        } catch (IOException e) {
            roomPhotoMap = Map.of("error","이미지 업로드에 실패했습니다: " + e.getMessage());
        }

        // TODO: S3Service를 바로 호출하면 됨. S3컨트롤러 필요 없음 (S3ControllerTest.java 삭제할지 결정 필요)
        List<String> fileUrl = new ArrayList<>(roomPhotoMap.values());

        //TODO: Address를 프론트에서 어떻게 받아오는지 확인 (일단 등록은 도로명주소로만 받음)
        addressRepository.save(request.getAddress());
        Room targetRoom = roomRepository.save(Room.make(request, fileUrl));

        for (EnrollmentTimeDto eachEnrollmentTime : request.getEnrollmentTimeDto()){
            Schedule eachSchedule = scheduleRepository.findByEnPhoneNumberAndDate(request.getEnPhoneNumber(), eachEnrollmentTime.getDate());
            if(eachSchedule != null){
                // 해당 날짜의 타임 테이블이 생성돼 있으면 이어서 추가
                eachSchedule.getSlotIndex().addAll(eachEnrollmentTime.getSelectedTimeSlotIndex());
                scheduleRepository.save(eachSchedule);
            }else{
                // 해당 날짜의 타임 테이블이 없으면 해당 날짜 Schedule 새로 만듦
                Schedule newSchedule = Schedule.make(eachEnrollmentTime, targetRoom);
                newSchedule.setRoom(targetRoom);
                scheduleRepository.save(newSchedule);
                targetRoom.getSchedules().add(newSchedule);
            }
        }

        return "등록 완료";
    }

    // 예약 기록을 확인한다.
    // 같은 날짜, 다른 시간대의 예약일 경우 한 날짜로 합쳐서 표시한다.
    public List<ReservationDto> accessToReservationRecords(PasswordRequest passwordRequest){
        List<Reservation> reservations = reservationRepository
                .findByRePhoneNumberOrderByDateAsc(passwordRequest.getPhoneNumber());

        Map<LocalDate, ReservationDto> dtoMap = new LinkedHashMap<>();

        for (Reservation reservation : reservations) {
            LocalDate date = reservation.getDate();

            dtoMap.computeIfAbsent(date, d -> ReservationDto.from(reservation))
                    .getReservedTime()
                    .addAll(reservation.getSlotIndex());
        }

        return new ArrayList<>(dtoMap.values());
    }

    // 등록 기록을 확인한다.
    // 같은 날짜, 다른 시간대의 예약일 경우 한 날짜로 합쳐서 표시한다.
    //TODO: 한 날짜에 하나만 Schedule이 생성될 수 있도록 로직 확인 필요, 위 함수에서 데이터가 Schedule -> EnrollmentDto 잘 이동되는지 확인
    public List<EnrollmentDto> accessToEnrollmentRecords(PasswordRequest passwordRequest) {
        List<Room> rooms = roomRepository.findByEnPhoneNumberWithSchedules(passwordRequest.getPhoneNumber());
//        List<Room> rooms = roomRepository.findByEnPhoneNumber(passwordRequest.getPhoneNumber());
        System.out.println(rooms.size());
        List<EnrollmentDto> enrollmentDtos = new ArrayList<>();
        for (Room eachRoom : rooms) {
            for (Schedule eachSchedule : eachRoom.getSchedules()) {
                enrollmentDtos.add(EnrollmentDto.from(eachRoom, eachSchedule));
            }
        }
        System.out.println(enrollmentDtos.size());

        return enrollmentDtos;
    }
}