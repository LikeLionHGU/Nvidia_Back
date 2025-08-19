package com.likelionhgu.nvidia.repository;

import com.likelionhgu.nvidia.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findByLatitudeAndLongitude(Double latitude, Double longitude);
}
