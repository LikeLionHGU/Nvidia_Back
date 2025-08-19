package com.likelionhgu.nvidia.repository;

import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByAddress(Address address);
    Room findByAddressAndPriceBetween(Address address, int minPrice, int maxPrice);
    @Query("SELECT r FROM Room r JOIN FETCH r.schedules s WHERE r.enPhoneNumber = :phoneNumber")
    List<Room> findByEnPhoneNumberWithSchedules(String phoneNumber);
}
