package com.likelionhgu.nvidia.repository;

import com.likelionhgu.nvidia.domain.Reservation;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByRePhoneNumber(String rePhoneNumber);
}
