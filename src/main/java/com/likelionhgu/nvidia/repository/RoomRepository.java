package com.likelionhgu.nvidia.repository;

import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByAddress(Address address);
    List<Room> findByEnPhoneNumber(String enPhoneNumber);
}
