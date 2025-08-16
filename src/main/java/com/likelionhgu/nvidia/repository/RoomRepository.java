package com.likelionhgu.nvidia.repository;

import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    //TODO: 레포지토리에서 제대로 가져올 수 있도록 함수명 네이밍 다시 하기
    Room findByRoomId(Long roomId);
    Room findByAddress(Address address);
    List<Room> findByPhoneNumber(String phoneNumber);
}
