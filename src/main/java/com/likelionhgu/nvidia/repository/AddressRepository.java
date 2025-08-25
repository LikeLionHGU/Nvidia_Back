package com.likelionhgu.nvidia.repository;

import com.likelionhgu.nvidia.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query(value = """
        SELECT a.*,
               ST_Distance_Sphere(POINT(a.longitude, a.latitude), POINT(:longitude, :latitude)) / 1000 AS distance_km
        FROM address a
        WHERE ST_Distance_Sphere(POINT(a.longitude, a.latitude), POINT(:longitude, :latitude)) <= :radiusInKm * 1000
        ORDER BY distance_km
        """, nativeQuery = true)
    List<Address> findByLocationWithinRadiusOrderByDistance(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radiusInKm") double radiusInKm  // ← 이름 맞추기
    );
}
