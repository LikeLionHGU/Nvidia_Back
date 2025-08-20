package com.likelionhgu.nvidia.repository;

import com.likelionhgu.nvidia.domain.Address;
import com.likelionhgu.nvidia.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Room r JOIN FETCH r.schedules s WHERE r.enPhoneNumber = :phoneNumber")
    List<Room> findByEnPhoneNumberWithSchedules(String phoneNumber);

    @Query(value = "SELECT r FROM Room r JOIN r.address a WHERE " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(a.latitude)) * cos(radians(a.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(a.latitude)))) <= :radius " +
            "AND r.price BETWEEN :minPrice AND :maxPrice " +
            "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(a.latitude)) * cos(radians(a.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(a.latitude)))) ASC")
    List<Room> findWithinRadiusAndPriceRange(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radius") double radius,
            @Param("minPrice") int minPrice,
            @Param("maxPrice") int maxPrice
    );
}
