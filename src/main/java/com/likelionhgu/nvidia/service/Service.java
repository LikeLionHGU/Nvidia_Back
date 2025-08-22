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
    private final FunctionService functionService;

    // request에 해당하는 공실을 repository에서 찾는다.
    public List<RoomBriefInfoDto> getRooms(AddressRequest request){
        double radiusInKm = 3.0;
        List<Address> nearbyAddresses = addressRepository.findByLocationWithinRadius(request.getLatitude(), request.getLongitude(), radiusInKm);

        return nearbyAddresses.stream().map(Address::getRoom).map(RoomBriefInfoDto::from).toList();
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
    public List<RoomBriefInfoDto> getRoomsWithPrompt(AddressAndPromptAndPricesRequest request){
        String prompt = request.getPrompt();
        CoordinateAddressDto midpoint = calculateMidpoint(AddressesForMiddleRequest.from(request.getAddressList()));

        // 거리기준(3km) 필터링 + 가격 기준 필터링
        List<Room> recommendedRooms = roomRepository.findWithinRadiusAndPriceRange(midpoint.getLatitude(),midpoint.getLongitude(), 3.0, request.getMinPrice(), request.getMaxPrice());
        System.out.println("3Km filtering : " + recommendedRooms.size());
        // 프롬프트 기준 필터링
        List<Room> recommendedRoomByPrompt = functionService.findRoomByChips(recommendedRooms, prompt);
        System.out.println("Prompt filtering : " + recommendedRoomByPrompt.size());
        // 거리기준 sorting
        List<Room> result = functionService.sortByDistance(recommendedRoomByPrompt, midpoint);

        return result.stream().map(RoomBriefInfoDto::from).toList();
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
    public String saveReservation(Long roomId, List<ReservationRequest> requests){
        for (ReservationRequest eachRequest : requests){
            Schedule schedule = scheduleRepository.findByRoomIdAndDate(roomId, LocalDate.parse(eachRequest.getDate()));
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Room not found"));
            //TODO: room에서 바로 schedule 접근해서 rePhoneNumber를 수정할 수 있다면 로직 수정 필요

            if (room != null) {
                Reservation reservation = Reservation.from(room, eachRequest);

                reservationRepository.save(reservation);
                schedule.setRePhoneNumber(eachRequest.getPhoneNumber());
            }
        }

        return "예약 완료";
    }

    // 등록 페이지에 입력된 정보들을 등록 기록(Room, Address, Schedule 각각)으로 저장한다.
    public String saveEnrollment(EnrollmentRequest request, List<MultipartFile> files){
        List<String> uploadUrlList = null;

        String uploadUrl = null;
        for(MultipartFile file : files) {
            try {
                uploadUrl = s3Service.uploadFiles(file, "roomPhoto/");
            } catch (IOException e) {
                System.out.println("error : 이미지 업로드에 실패했습니다: " + e.getMessage());
            }
            uploadUrlList.add(uploadUrl);
        }



        addressRepository.save(Address.from(request.getLatitude(), request.getLongitude(), request.getRoadName()));
        Room targetRoom = roomRepository.save(Room.make(request, uploadUrlList));

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
        List<Reservation> reservations = reservationRepository.findByPhoneNumberOrderByDateAsc(passwordRequest.getPhoneNumber());
        List<ReservationDto> reservationDtos = reservations.stream().map(ReservationDto::from).toList();
        Map<LocalDate, ReservationDto> reservationMap = new LinkedHashMap<>();

        for (ReservationDto eachReservationDto : reservationDtos) {
            LocalDate selectedDate = eachReservationDto.getReservedDate();

            if (reservationMap.containsKey(selectedDate)) {
                reservationMap.get(selectedDate).getReservedTime().addAll(eachReservationDto.getReservedTime());
            }else{
                reservationMap.put(selectedDate, eachReservationDto);
            }
        }

        return new ArrayList<>(reservationMap.values());
    }

    // 등록 기록을 확인한다.
    // 같은 날짜, 다른 시간대의 예약일 경우 한 날짜로 합쳐서 표시한다.
    //TODO: OrderedBy 필요 없는지 확인 필요
    public List<EnrollmentDto> accessToEnrollmentRecords(PasswordRequest passwordRequest) {
        List<Room> rooms = roomRepository.findByEnPhoneNumberWithSchedulesOrderByDateAsc(passwordRequest.getPhoneNumber());
        List<EnrollmentDto> enrollmentDtos = new ArrayList<>();
        for (Room eachRoom : rooms) {
            for (Schedule eachSchedule : eachRoom.getSchedules()) {
                enrollmentDtos.add(EnrollmentDto.from(eachRoom, eachSchedule));
            }
        }

        return enrollmentDtos;
    }
}