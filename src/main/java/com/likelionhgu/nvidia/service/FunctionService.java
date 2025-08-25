package com.likelionhgu.nvidia.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelionhgu.nvidia.ai.ChipExtractionService;
import com.likelionhgu.nvidia.ai.ChipJSON;
import com.likelionhgu.nvidia.ai.ChipsJSON;
import com.likelionhgu.nvidia.ai.GeminiApiClient;
import com.likelionhgu.nvidia.controller.request.AddressRequest;
import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.domain.Room;
import com.likelionhgu.nvidia.dto.CoordinateAddressDto;
import com.likelionhgu.nvidia.repository.AddressRepository;
import com.likelionhgu.nvidia.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FunctionService {
    private final RoomRepository roomRepository;
    private final AddressRepository addressRepository;
    private final ChipExtractionService chipExtractionService;
    private final GeminiApiClient geminiApiClient;
    private final ObjectMapper objectMapper;

    private double distanceInKm(double lat1Deg, double lon1Deg, double lat2Deg, double lon2Deg) {
        final double R = 6371.0; // km
        double lat1 = Math.toRadians(lat1Deg);
        double lon1 = Math.toRadians(lon1Deg);
        double lat2 = Math.toRadians(lat2Deg);
        double lon2 = Math.toRadians(lon2Deg);

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double hav = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        return R * 2 * Math.atan2(Math.sqrt(hav), Math.sqrt(1 - hav));
    }



    public CoordinateAddressDto midAvg(List<CoordinateAddressDto> list) {
        if (list == null || list.isEmpty()) throw new IllegalArgumentException("비어 있음");
        double latSum = 0, lonSum = 0;
        int n = 0;
        for (CoordinateAddressDto a : list) {
            if (a == null) continue;
            latSum += a.getLatitude();
            lonSum += a.getLongitude();
            n++;
        }
        if (n == 0) throw new IllegalArgumentException("유효 항목 없음");
        CoordinateAddressDto dto = CoordinateAddressDto.builder()
                .latitude(latSum / n)
                .longitude(lonSum / n)
                .build();
        return dto;
    }


    public List<Room> findRoomBy3km(List<AddressRequest> addressRequestList){
        List<Address> addressList = addressRepository.findAll();

        List<CoordinateAddressDto> coordinateAddressDtoList = addressRequestList.stream().map(CoordinateAddressDto::from).toList();
        CoordinateAddressDto dto = midAvg(coordinateAddressDtoList);
        double centerLat = dto.getLatitude(); // center latitude
        double centerLon = dto.getLongitude(); // center longitude

        //dto와 addressList를 각각 비교해서 3km이내에 있는 Address만 남기고 나머지는 List에서 제외시키기
        List<Address> filtered = new ArrayList<>();
        for (Address addr : addressList) {
            if (addr == null) continue;
            double distKm = distanceInKm(centerLat, centerLon, addr.getLatitude(), addr.getLongitude());
            if (distKm <= 3.0) {
                filtered.add(addr);
            }
        }

        List<Room> roomList = new ArrayList<>();
        for(Address address : filtered){
            roomList.add(address.getRoom());
        }
        return roomList;
    }

    public List<Room> findRoomByBudgetRange(List<Room> roomList, int minPrice, int maxPrice){
        List<Room> filtered = new ArrayList<>();
        for(Room room : roomList){
            if(room.getPrice() <= maxPrice && room.getPrice() >= minPrice){
                filtered.add(room);
            }
        }

        return filtered;
    }

    public List<Room> findRoomByChips(List<Room> roomList, String prompt) {
        try {
            // --- 1) Gemini 호출 (다른 분석들과 동일한 패턴) ---
            String chipsJson = geminiApiClient.generateChipsJson(prompt);

            // --- 2) JSON 파싱 (readTree → treeToValue) ---
            JsonNode chipsNode = objectMapper.readTree(chipsJson);
            ChipsJSON chips = objectMapper.treeToValue(chipsNode, ChipsJSON.class);

            if (chips == null || chips.getChips() == null || chips.getChips().isEmpty()) {
                return List.of();
            }

            Set<String> desired = chips.getChips().stream()
                    .map(ChipJSON::getChip)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            // --- 3) 겹치는 칩 개수로 스코어링 & 정렬 ---
            Map<Room, Long> matchCounts = new HashMap<>();
            for (Room room : roomList) {
                if (room.getChipList() == null || room.getChipList().isEmpty())
                    continue;

                long matches = room.getChipList().stream()
                        .filter(Objects::nonNull)
                        .filter(desired::contains)
                        .count();

                if (matches > 0) {
                    matchCounts.put(room, matches);
                }
            }

            List<Room> filtered = new ArrayList<>(matchCounts.keySet());
            filtered.sort(Comparator.comparingLong(matchCounts::get).reversed());

            return filtered;

        } catch (Exception e) {
            // 파싱 실패나 API 오류 시 안전하게 빈 결과
            return List.of();
        }
    }

    public List<Room> sortByDistance(List<Room> roomList, CoordinateAddressDto midpoint){
        if (roomList == null || roomList.isEmpty()) {
            return roomList;
        }

        // Comparator를 사용하여 거리를 기준으로 정렬
        // 람다식을 사용하여 코드를 간결하게 작성
        roomList.sort(Comparator.comparingDouble(room ->
                calculateDistance(
                        midpoint.getLatitude(),
                        midpoint.getLongitude(),
                        room.getAddress().getLatitude(),
                        room.getAddress().getLongitude()
                )
        ));

        return roomList;
    }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double EARTH_RADIUS_KM = 6371; // 지구 반지름 (킬로미터)

        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c; // 거리(킬로미터) 반환
    }


}
