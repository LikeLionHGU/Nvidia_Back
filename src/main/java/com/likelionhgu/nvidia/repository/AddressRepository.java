package com.likelionhgu.nvidia.repository;

import com.likelionhgu.nvidia.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findByLatitudeAndLongitude(Double latitude, Double longitude);

    @Query(value = "SELECT a FROM Address a WHERE " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(a.latitude)) * cos(radians(a.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(a.latitude)))) <= :radius")
    List<Address> findByLocationWithinRadius(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radius") double radius
    );
}