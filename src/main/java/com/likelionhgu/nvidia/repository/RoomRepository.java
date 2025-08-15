package com.likelionhgu.nvidia.repository;

import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByRoomId(Long roomId);
    List<Room> findByAddress(Address address);
}
