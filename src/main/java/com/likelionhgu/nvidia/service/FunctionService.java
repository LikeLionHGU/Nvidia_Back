package com.likelionhgu.nvidia.service;

import com.likelionhgu.nvidia.ai.ChipExtractorService;
import com.likelionhgu.nvidia.controller.request.AddressRequest;
import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.domain.Chip;
import com.likelionhgu.nvidia.domain.Room;
import com.likelionhgu.nvidia.dto.AddressDto;
import com.likelionhgu.nvidia.dto.CoordinateAddressDto;
import com.likelionhgu.nvidia.repository.AddressRepository;
import com.likelionhgu.nvidia.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FunctionService {
    private final RoomRepository roomRepository;
    private final AddressRepository addressRepository;
    private final ChipExtractorService chipExtractorService;

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



    public AddressDto midAvg(List<AddressDto> list) {
        if (list == null || list.isEmpty()) throw new IllegalArgumentException("비어 있음");
        double latSum = 0, lonSum = 0;
        int n = 0;
        for (AddressDto a : list) {
            if (a == null) continue;
            latSum += a.getLatitude();
            lonSum += a.getLongitude();
            n++;
        }
        if (n == 0) throw new IllegalArgumentException("유효 항목 없음");
        AddressDto dto = AddressDto.builder()
                .latitude(latSum / n)
                .longitude(lonSum / n)
                .build();
        return dto;
    }


    public List<Room> findRoomBy3km(List<AddressRequest> addressRequestList){
        List<Address> addressList = addressRepository.findAll();

        List<AddressDto> addressDtoList = addressRequestList.stream().map(AddressDto::from).toList();
        AddressDto dto = midAvg(addressDtoList);
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
            if(room.getPrice() <= maxPrice && room.getPrice() <= minPrice){
                filtered.add(room);
            }
        }

        return filtered;
    }

    public List<Room> findRoomByChips(List<Room> roomList, String prompt) {
        List<Chip> chips = chipExtractorService.extractChips(prompt).getChips();

        List<Room> filtered = new ArrayList<>();
        for (Room room : roomList) {
            if (room == null || room.getChipList() == null) continue;

            // room.chipList와 chips 중 하나라도 일치하면 통과
            boolean match = false;
            for (Chip chip : chips) {
                if (room.getChipList().contains(chip.name())) {
                    match = true;
                    break;
                }
            }

            if (match) {
                filtered.add(room);
            }
        }

        return filtered;
    }


}
